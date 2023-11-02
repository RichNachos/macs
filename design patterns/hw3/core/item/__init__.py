from dataclasses import dataclass

from core.product import Product


@dataclass
class Item:
    item_name: str
    product: Product
    units: int

    def get_total(self) -> float:
        return round(self.product.price * self.units, 2)
