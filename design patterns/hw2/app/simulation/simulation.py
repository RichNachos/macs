from app import constants as c
from app.classes.cashier import Cashier
from app.classes.customer import Customer
from app.classes.discount_manager import GreedyDiscountManager
from app.classes.manager import Manager
from app.classes.receipt import ReceiptFactory
from app.classes.register import Register
from app.simulation.shop_dao import ShopDao


class Simulation:
    def __init__(self, dao: ShopDao):
        self.dao = dao

    def run(self) -> None:
        register = Register(
            ReceiptFactory(
                GreedyDiscountManager(discounts=self.dao.fetch_all_discounts())
            )
        )
        cashier = Cashier(register, Manager())
        shop = self.dao.fetch_all_items()

        while cashier.shift != c.NUM_SHIFTS:
            new_customer = Customer()
            new_customer.shop_around(shop)

            for item in new_customer.cart:
                cashier.register_item(item)

            cashier.show_items()
            cashier.pay(new_customer.choose_payment_method())

        print("\n-----------------------SIMULATION ENDED--------------------------\n")
