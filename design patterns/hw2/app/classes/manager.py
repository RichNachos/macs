from dataclasses import dataclass

from app import constants as c


@dataclass
class Manager:
    def __init__(self) -> None:
        self.sale_count: int = 0

    def notify_sale(self) -> str | None:
        self.sale_count += 1
        if self.sale_count % c.Z_REPORT_INTERVAL == 0:
            print("Do a Z report? (y/n)")
            answer = input()
            if answer == "y":
                return c.Z_REPORT
            else:
                return None
        elif self.sale_count % c.X_REPORT_INTERVAL == 0:
            print("Do a X report? (y/n)")
            answer = input()
            if answer == "y":
                return c.X_REPORT
            else:
                return None
        else:
            return None
