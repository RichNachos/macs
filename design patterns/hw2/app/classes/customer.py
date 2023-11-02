import random
from typing import List

from app import constants as c
from app.classes.item import Item


class Customer:
    def __init__(self) -> None:
        self.cart: List[Item] = []

    def shop_around(self, store: List[Item]) -> None:
        for item in store:
            if random.random() < (1.0 / len(store)) * 5.0:
                self.cart.append(item)

    def choose_payment_method(self) -> str:
        if random.random() < 0.5:
            return c.CASH
        else:
            return c.CARD

    def finish_shopping(self) -> List[Item]:
        return self.cart
