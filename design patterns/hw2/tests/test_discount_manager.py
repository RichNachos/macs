from app.classes.discount_manager import GreedyDiscountManager
from app.classes.discounts import Discount
from app.classes.item import Item
from app.classes.product import Product


def test_discount_manager() -> None:
    product1 = Product("Milk", 10.00)
    product2 = Product("Coca Cola", 2.00)
    item1 = Item(product1.get_name(), product1, 1)
    item2 = Item(product2.get_name(), product2, 1)
    discount1 = Discount(0.05, [item1])
    discount2 = Discount(0.10, [item2])
    cart = [item1, item2]

    discount_list = [discount1, discount2]
    discount_manager = GreedyDiscountManager(discount_list)
    assert discount_manager.best_discount(cart) == discount1
    cart.remove(item1)
    assert discount_manager.best_discount(cart) == discount2
    cart.append(item1)

    discount3 = Discount(0.15, [item1, item2])
    discount_list.append(discount3)
    discount_manager = GreedyDiscountManager(discount_list)
    assert discount_manager.best_discount(cart) == discount3

    discount4 = Discount(0.95, [item1])
    discount_list.append(discount4)
    discount_manager = GreedyDiscountManager(discount_list)
    assert discount_manager.best_discount(cart) == discount4
