from app import constants as c
from app.classes.discount_manager import GreedyDiscountManager
from app.classes.item import Item
from app.classes.product import Product
from app.classes.receipt import ReceiptFactory
from app.classes.register import Register


def test_register() -> None:
    register = Register(ReceiptFactory(GreedyDiscountManager()))

    item1 = Item("Milk", Product("Milk", 4.00), 1)
    register.open_receipt()
    register.register_item(item1)
    assert register.show_items() == [("Milk", 1, 4.00, 4.00)]
    assert register.revenue == 0.00

    register.pay(c.CASH)
    assert register.revenue == 4.00
    assert register.data == [("Milk", 1)]
    register.close_receipt()

    register.open_receipt()
    register.register_item(item1)
    register.pay(c.CARD)
    assert register.revenue == 8.00
    assert register.data == [("Milk", 2)]
    register.close_receipt()

    register.open_receipt()
    item2 = Item("Milk Pack", Product("Milk", 4.00), 6)
    register.register_item(item2)
    register.pay(c.CASH)
    assert register.revenue == 32.00
    assert register.data == [("Milk", 8)]
    register.close_receipt()

    register.clear()
    assert register.revenue == 0.00
    assert register.data == []
