from dataclasses import dataclass
from sqlite3 import Connection
from typing import List

from core.item import Item
from core.product import Product
from core.receipt import Receipt
from core.report import Report, ReportRow
from infra.dao import Dao


@dataclass
class SqliteDao(Dao):
    con: Connection

    def open_receipt(self) -> int:
        cur = self.con.cursor()
        cur.execute("INSERT INTO receipts(status) values(1)")
        assert cur.lastrowid is not None
        self.con.commit()
        return cur.lastrowid

    def close_receipt(self, receipt_id: int) -> None:
        cur = self.con.cursor()
        cur.execute("UPDATE receipts SET status = 0 WHERE receipt_id = ?", [receipt_id])
        self.con.commit()

    def register_item(self, receipt_id: int, item_id: int) -> bool:
        if (
            not self.receipt_exists(receipt_id)
            or not self.receipt_status(receipt_id)
            or not self.item_exists(item_id)
        ):
            return False
        cur = self.con.cursor()
        cur.execute(
            "INSERT INTO receipt_items(receipt_id, item_id) values(?,?)",
            (receipt_id, item_id),
        )
        self.con.commit()
        return True

    def receipt_status(self, receipt_id: int) -> bool:
        cur = self.con.cursor()
        res = cur.execute(
            "SELECT status FROM receipts WHERE receipt_id = ?", [receipt_id]
        )
        data = res.fetchone()
        if data is not None:
            if data[0] == 1:
                return True
        return False

    def report(self) -> Report:
        revenue = 0.00
        list: List[ReportRow] = []
        items: List[Item] = []

        cur = self.con.cursor()
        res = cur.execute("SELECT receipt_id FROM receipts WHERE status = 0")
        closed_receipts = res.fetchall()
        closed_count = len(closed_receipts)
        for closed_receipt_data in closed_receipts:
            res = cur.execute(
                "SELECT item_id FROM receipt_items WHERE receipt_id = ?",
                [closed_receipt_data[0]],
            )
            items_ids = res.fetchall()
            for item_id in items_ids:
                res = cur.execute(
                    "SELECT name,product_id,units FROM items WHERE item_id = ?",
                    [item_id[0]],
                )
                items_data = res.fetchall()
                for item_data in items_data:
                    res = cur.execute(
                        "SELECT name, price FROM products WHERE product_id = ?",
                        [item_data[1]],
                    )
                    product_data = res.fetchone()
                    product = Product(product_data[0], product_data[1])
                    item = Item(item_data[0], product, item_data[2])
                    items.append(item)
                    revenue = revenue + item.get_total()

        for item in items:
            flag = True
            for row in list:
                if row.item_name == item.product.name:
                    flag = False
                    row.units = row.units + item.units
                    break
            if flag:
                list.append(ReportRow(item.product.name, item.units))

        return Report(revenue, list, closed_count)

    def get_receipt(self, receipt_id: int) -> Receipt:
        cur = self.con.cursor()
        res = cur.execute(
            "SELECT item_id FROM receipt_items WHERE receipt_id = ?", [receipt_id]
        )
        list = []
        grand_total = 0.00

        data = res.fetchall()
        for tup in data:
            item_id = tup[0]
            res = cur.execute("SELECT * FROM items WHERE item_id = ?", [item_id])
            item_data = res.fetchone()

            res = cur.execute(
                "SELECT * FROM products WHERE product_id = ?", [item_data[2]]
            )
            product_data = res.fetchone()
            product = Product(product_data[1], product_data[2])
            item = Item(item_data[1], product, item_data[3])
            list.append(item)
            grand_total = grand_total + item.get_total()
        receipt = Receipt(list, grand_total)
        return receipt

    def receipt_exists(self, receipt_id: int) -> bool:
        cur = self.con.cursor()
        res = cur.execute("SELECT * FROM receipts WHERE receipt_id = ?", [receipt_id])
        data = res.fetchone()
        if data is not None:
            return True
        return False

    def item_exists(self, item_id: int) -> bool:
        cur = self.con.cursor()
        res = cur.execute("SELECT * FROM items WHERE item_id = ?", [item_id])
        data = res.fetchone()
        if data is not None:
            return True
        return False
