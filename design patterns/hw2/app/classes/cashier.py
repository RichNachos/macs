from dataclasses import dataclass

from app import constants as c
from app.classes.item import Item
from app.classes.manager import Manager
from app.classes.register import Register
from app.classes.reporter import Reporter


@dataclass
class Cashier:
    def __init__(self, register: Register, manager: Manager):
        self.register = register
        self.manager: Manager = manager  # Only one observer needed
        self.shift = 0
        self.reporter = Reporter()

    def make_x_report(self) -> None:
        print(self.reporter.make_x_report(self.register.data, self.register.revenue))
        input("Press enter to continue")

    def make_z_report(self) -> None:
        self.shift += 1
        self.register.clear()
        print("Shift ended")

    def register_item(self, item: Item) -> None:
        self.register.register_item(item)

    def remove_item(self, item: Item) -> None:
        self.register.remove_item(item)

    def show_items(self) -> None:
        print(
            self.reporter.make_receipt(
                self.register.show_items(), self.register.receipt.get_sum()
            )
        )

    def pay(self, payment_method: str) -> None:
        self.register.pay(payment_method)
        self.register.close_receipt()
        manager_order = self.manager.notify_sale()
        if manager_order == c.X_REPORT:
            self.make_x_report()
        elif manager_order == c.Z_REPORT:
            self.make_z_report()

        if self.shift != c.NUM_SHIFTS:
            self.register.open_receipt()
            print("\n-------------------------------------------------\n")
        else:
            print("Cashier went home")
