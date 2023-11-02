from __future__ import annotations

from dataclasses import dataclass

from app.classes.product import Product


@dataclass
class Item:
    item_name: str
    product: Product
    units: int

    def get_total(self) -> float:
        return round(self.product.price * self.units, 2)

    def log(self) -> str:
        txt = "| {name} | {units} | {price} | {total} |"
        return txt.format(
            name=self.item_name,
            units=self.units,
            price=self.product.price,
            total=self.get_total(),
        )

    def product_equals(self, item2: Item) -> bool:
        return self.product.equals(item2.product)

    def equals(self, item2: Item) -> bool:
        return (
            self.product.equals(item2.product)
            and self.units == item2.units
            and self.item_name == item2.item_name
        )
