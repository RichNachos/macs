from abc import abstractmethod
from dataclasses import dataclass, field
from typing import List, Optional

from app.classes.discounts import Discount
from app.classes.item import Item


@dataclass
class DiscountManager:
    discounts: List[Discount] = field(default_factory=list)

    @abstractmethod
    def best_discount(self, items: List[Item]) -> Optional[Discount]:
        pass


@dataclass
class GreedyDiscountManager(DiscountManager):
    def best_discount(self, items: List[Item]) -> Optional[Discount]:
        raw_total = 0.00
        for item in items:
            raw_total += item.get_total()

        discounted_total = raw_total
        best_discount = None

        for discount in self.discounts:
            if discount.discount_applicable(items):
                tmp_total = raw_total
                for item in discount.required_items:
                    tmp_total = (
                        tmp_total
                        - item.get_total()
                        + discount.discounted_price(item.get_total())
                    )
                if tmp_total < discounted_total:
                    discounted_total = tmp_total
                    best_discount = discount

        return best_discount
        pass
