from dataclasses import dataclass
from typing import List, Tuple

from app.classes.discount_manager import DiscountManager
from app.classes.item import Item


class Receipt:
    def __init__(self, mngr: DiscountManager):
        self.discount_manager = mngr
        self.items: List[Item] = []
        self.status = False

    def open(self) -> None:
        self.status = True

    def register_item(self, item: Item) -> None:
        if self.status:
            self.items.append(item)

    def remove_item(self, item: Item) -> None:
        if self.status:
            self.items.remove(item)

    def close(self) -> None:
        self.status = False

    def get_sum(self) -> float:
        total_sum = 0.00
        for tup in self.get_contents():
            total_sum += tup[3]
        return total_sum

    def get_contents(self) -> List[Tuple[str, int, float, float]]:
        current_items = self.items.copy()
        table = []
        while self.discount_manager.best_discount(current_items) is not None:
            discount = self.discount_manager.best_discount(current_items)
            assert discount is not None

            for item in discount.required_items:
                row = (
                    item.item_name,
                    item.units,
                    item.product.price,
                    discount.discounted_price(item.get_total()),
                )
                table.append(row)
                current_items.remove(item)
        for item in current_items:
            row = (item.item_name, item.units, item.product.price, item.get_total())
            table.append(row)
        return table


@dataclass
class ReceiptFactory:
    discount_manager: DiscountManager

    def build(self) -> Receipt:
        return Receipt(self.discount_manager)
