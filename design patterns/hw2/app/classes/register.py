from typing import List, Tuple

from app.classes.item import Item
from app.classes.receipt import ReceiptFactory


class Register:
    def __init__(self, receipt_factory: ReceiptFactory):
        self.receipt_factory = receipt_factory
        self.revenue = 0.00
        self.data: List[Tuple[str, int]] = []
        self.receipt = receipt_factory.build()

    def open_receipt(self) -> None:
        self.receipt = self.receipt_factory.build()
        self.receipt.open()

    def close_receipt(self) -> None:
        self.receipt.close()

    def register_item(self, item: Item) -> None:
        self.receipt.register_item(item)

    def remove_item(self, item: Item) -> None:
        self.receipt.remove_item(item)

    def show_items(self) -> List[Tuple[str, int, float, float]]:
        return self.receipt.get_contents()

    def pay(self, payment_method: str) -> None:
        self.revenue += self.receipt.get_sum()
        print("Customer paid with " + payment_method.lower())

        for item in self.receipt.items:
            flag = False
            for tup in self.data:
                if item.product.get_name() in tup:
                    self.data.remove(tup)
                    self.data.append((item.product.get_name(), item.units + tup[1]))
                    flag = True
                    break
            if not flag:
                self.data.append((item.product.get_name(), item.units))

    def clear(self) -> None:
        self.data.clear()
        self.revenue = 0.00
