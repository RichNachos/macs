# from typing import Protocol
from __future__ import annotations

from dataclasses import dataclass


@dataclass
class Product:
    name: str
    price: float

    def get_name(self) -> str:
        return self.name

    def get_price(self) -> float:
        return self.price

    def equals(self, product2: Product) -> bool:
        return self.name == product2.name and self.price == product2.price
