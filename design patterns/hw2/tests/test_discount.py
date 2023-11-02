from app.classes.discounts import Discount
from app.classes.item import Item
from app.classes.product import Product


def test_discount() -> None:
    product1 = Product("Milk", 3.99)
    product2 = Product("Coca Cola", 1.99)
    item1 = Item(product1.get_name(), product1, 1)
    item2 = Item(product2.get_name(), product2, 1)
    discount = Discount(0.05, [item1])
    cart = [item1, item2]
    assert discount.discount_applicable(cart)
    cart = [item2]
    assert not discount.discount_applicable(cart)

    assert discount.discounted_price(1.00) == 0.95
    assert discount.discounted_price(2.00) == 1.90
