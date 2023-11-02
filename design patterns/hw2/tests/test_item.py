from app.classes.item import Item
from app.classes.product import Product


def test_item() -> None:
    product_name = "Milk"
    item_name = "Milk Pack"
    product_price = 4.99
    product_amount = 5

    product = Product(product_name, product_price)
    item1 = Item(product_name, product, 1)
    assert item1.get_total() == round(product_price * 1, 2)
    assert item1.log() == "| {name} | {units} | {price} | {total} |".format(
        name=product_name,
        units=1,
        price=product_price,
        total=product_price,
    )

    item2 = Item(item_name, product, product_amount)
    assert item2.get_total() == round(product_price * product_amount, 2)
    assert item2.log() == "| {name} | {units} | {price} | {total} |".format(
        name=item_name,
        units=product_amount,
        price=product_price,
        total=round(product_price * product_amount, 2),
    )

    assert item1.product_equals(item2)

    item1 = Item(item_name, product, product_amount)
    assert item1.equals(item2)
    item1 = Item(item_name, product, product_amount - 1)
    assert not item1.equals(item2)
