from dataclasses import dataclass
from typing import List

from app.classes.item import Item


@dataclass
class Discount:
    discount: float
    required_items: List[Item]

    def discount_applicable(self, items: List[Item]) -> bool:
        for req_item in self.required_items:
            flag = False
            for item in items:
                if item.equals(req_item):
                    flag = True
                    break
            if not flag:
                return False
        return True

    def discounted_price(self, price: float) -> float:
        return round(price * (1 - self.discount), 2)
