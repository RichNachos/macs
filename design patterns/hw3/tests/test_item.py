from core.item import Item
from core.product import Product


def test_item() -> None:
    product_name = "Milk"
    item_name = "Milk Pack"
    product_price = 4.99
    product_amount = 5

    product = Product(product_name, product_price)
    item1 = Item(product_name, product, 1)
    assert item1.get_total() == round(product_price * 1, 2)

    item2 = Item(item_name, product, product_amount)
    assert item2.get_total() == round(product_price * product_amount, 2)

    assert item1.product.__eq__(item2.product)

    item1 = Item(item_name, product, product_amount)
    assert item1.__eq__(item2)
    item1 = Item(item_name, product, product_amount - 1)
    assert not item1.__eq__(item2)
