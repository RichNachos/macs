from app.classes.discount_manager import GreedyDiscountManager
from app.classes.item import Item
from app.classes.product import Product
from app.classes.receipt import Receipt


def test_receipt() -> None:
    discount_manager = GreedyDiscountManager([])
    receipt = Receipt(discount_manager)
    product1 = Product("Milk", 4.00)
    product2 = Product("Coca Cola", 1.50)
    item1 = Item(product1.get_name(), product1, 1)
    item2 = Item(product2.get_name(), product2, 1)

    receipt.register_item(item1)
    assert receipt.get_sum() == 0.00
    receipt.open()
    receipt.register_item(item1)
    assert receipt.get_sum() == 4.00
    receipt.register_item(item1)
    assert receipt.get_sum() == 8.00
    receipt.register_item(item2)
    assert receipt.get_sum() == 9.50
    receipt.remove_item(item1)
    assert receipt.get_sum() == 5.50
    receipt.close()
    receipt.remove_item(item1)
    receipt.register_item(item2)
    assert receipt.get_sum() == 5.50

    receipt.open()
    receipt.remove_item(item2)
    assert receipt.get_sum() == 4.00

    assert receipt.get_contents() == [("Milk", 1, 4.00, 4.00)]
    receipt.register_item(item1)
    assert receipt.get_contents() == [("Milk", 1, 4.00, 4.00), ("Milk", 1, 4.00, 4.00)]
    receipt.remove_item(item1)
    receipt.register_item(item2)
    assert receipt.get_contents() == [
        ("Milk", 1, 4.00, 4.00),
        ("Coca Cola", 1, 1.50, 1.50),
    ]
    receipt.close()
    receipt.items = []
