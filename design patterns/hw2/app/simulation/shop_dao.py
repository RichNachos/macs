from typing import List

from app.classes import shop_data
from app.classes.discounts import Discount
from app.classes.item import Item


class ShopDao:
    def fetch_all_items(self) -> List[Item]:
        return shop_data.SHOP

    def fetch_all_discounts(self) -> List[Discount]:
        return shop_data.DISCOUNTS
