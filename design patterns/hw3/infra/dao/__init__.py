import sqlite3
from abc import abstractmethod
from dataclasses import dataclass
from typing import Protocol

from core.receipt import Receipt
from core.report import Report


def build_database(db_name: str) -> None:
    con = sqlite3.connect(db_name)
    cur = con.cursor()
    # cur.execute("DROP TABLE IF EXISTS products")
    # cur.execute("DROP TABLE IF EXISTS items")
    # cur.execute("DROP TABLE IF EXISTS receipts")
    # cur.execute("DROP TABLE IF EXISTS receipt_items")
    cur.execute(
        "CREATE TABLE IF NOT EXISTS products("
        "product_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "name TEXT, price REAL)"
    )
    cur.execute(
        "CREATE TABLE IF NOT EXISTS items("
        "item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "name TEXT, "
        "product_id INTEGER, units INTEGER, "
        "CONSTRAINT fk_products "
        "FOREIGN KEY (product_id) "
        "REFERENCES products(product_id))"
    )
    cur.execute(
        "CREATE TABLE IF NOT EXISTS receipts("
        "receipt_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "status INTEGER)"
    )
    cur.execute(
        "CREATE TABLE IF NOT EXISTS receipt_items("
        "entry_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "receipt_id INTEGER, "
        "item_id INTEGER, "
        "CONSTRAINT fk_receipts "
        "FOREIGN KEY (receipt_id) "
        "REFERENCES receipts(receipt_id),"
        "CONSTRAINT fk_items "
        "FOREIGN KEY (item_id) "
        "REFERENCES items(item_id))"
    )
    # cur.execute("INSERT INTO products(name, price) values('Milk', 2.95)")
    # cur.execute("INSERT INTO products(name, price) values('Coca Cola', 1.50)")
    # cur.execute(
    #     "INSERT INTO items(name, product_id, units) values(?,?,?)",
    #     ("Milk", 1, 1),
    # )
    # cur.execute(
    #     "INSERT INTO items(name, product_id, units) values(?,?,?)",
    #     ("Milk Carton", 1, 6),
    # )
    # cur.execute(
    #     "INSERT INTO items(name, product_id, units) values(?,?,?)",
    #     ("Coca Cola", 2, 1),
    # )
    con.commit()


@dataclass
class Dao(Protocol):
    @abstractmethod
    def open_receipt(self) -> int:
        pass

    @abstractmethod
    def close_receipt(self, receipt_id: int) -> None:
        pass

    @abstractmethod
    def register_item(self, receipt_id: int, item_id: int) -> bool:
        pass

    @abstractmethod
    def receipt_status(self, receipt_id: int) -> bool:
        pass

    @abstractmethod
    def report(self) -> Report:
        pass

    @abstractmethod
    def get_receipt(self, receipt_id: int) -> Receipt:
        pass

    @abstractmethod
    def receipt_exists(self, receipt_id: int) -> bool:
        pass

    @abstractmethod
    def item_exists(self, item_id: int) -> bool:
        pass
