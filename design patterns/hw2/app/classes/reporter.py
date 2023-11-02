from typing import List, Tuple

from tabulate import tabulate


class Reporter:
    def make_receipt(
        self, table: List[Tuple[str, int, float, float]], total_sum: float
    ) -> str:
        data = (
            tabulate(
                table,
                headers=["Name", "Units", "Price", "Total"],
                tablefmt="orgtbl",
                floatfmt=".2f",
            )
            + "\n\nSum: "
            + "{:.2f}".format(total_sum)
            + "\n"
        )
        return data

    def make_x_report(self, table: List[Tuple[str, int]], revenue: float) -> str:
        data = (
            tabulate(table, headers=["Name", "Sold"], tablefmt="orgtbl", floatfmt=".2f")
            + "\n\nTotal Revenue: "
            + "{:.2f}".format(revenue)
            + "\n"
        )
        return data
        pass
