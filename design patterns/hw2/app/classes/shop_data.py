from typing import List

from app.classes.discounts import Discount
from app.classes.item import Item
from app.classes.product import Product

PRODUCTS: List[Product] = [
    Product("Milk", 1.65),
    Product("Egg", 0.25),
    Product("Cheese", 3.55),
    Product("Bread", 0.80),
    Product("Rice", 1.20),
    Product("Butter", 3.30),
    Product("Pasta", 2.85),
    Product("Mineral Water", 0.50),
    Product("Coca Cola", 0.70),
    Product("Beer", 3.50),
    Product("Doritos", 2.30),
    Product("Snickers", 1.05),
    Product("Sulfuric Acid", 35.50),
    Product("Acetone", 11.70),
    Product("Methylamine", 85.00),
    Product("Blue Sky", 120.00),
]

SHOP: List[Item] = []
for product in PRODUCTS:
    SHOP.append(Item(product.get_name(), product, 1))

PACKS: List[Item] = [
    Item("Egg Carton", PRODUCTS[1], 12),
    Item("Milk Pack", PRODUCTS[0], 6),
    Item("Coca Cola Pack", PRODUCTS[8], 4),
    Item("Beer Pack", PRODUCTS[9], 6),
    Item("Methylamine Tanker", PRODUCTS[14], 30),
    Item("Body Hider", PRODUCTS[12], 6),
    Item("Los Pollos Hermanos Chicken", PRODUCTS[15], 10),
]
SHOP.extend(PACKS)

DISCOUNTS: List[Discount] = [
    Discount(0.05, [SHOP[2], SHOP[3]]),
    Discount(0.10, [PACKS[0]]),
    Discount(0.20, [PACKS[1]]),
    Discount(0.15, [PACKS[2]]),
    Discount(0.05, [PACKS[3]]),
    Discount(0.05, [SHOP[6], SHOP[7]]),
    Discount(0.10, [SHOP[12], SHOP[13]]),
    Discount(0.85, [SHOP[14], SHOP[15], SHOP[16]]),
]
