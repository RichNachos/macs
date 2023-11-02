import sqlite3

from core.report import ReportRow
from infra.dao.sqlite_dao import SqliteDao

DB_NAME = "test_db"


def test_sqlitedao() -> None:
    con = sqlite3.connect(DB_NAME)
    dao = SqliteDao(con)
    cur = con.cursor()
    cur.execute("DROP TABLE IF EXISTS products")
    cur.execute("DROP TABLE IF EXISTS items")
    cur.execute("DROP TABLE IF EXISTS receipts")
    cur.execute("DROP TABLE IF EXISTS receipt_items")
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
    # prod = Product("Milk", 2.95)
    # item1 = Item("Milk", prod, 1)
    # item2 = Item("Milk Carton", prod, 6)

    cur.execute("INSERT INTO products(name, price) values('Milk', 2.95)")
    res = cur.execute("SELECT product_id FROM products WHERE name = 'Milk'")
    product_id = res.fetchone()[0]
    assert product_id == 1
    cur.execute(
        "INSERT INTO items(name, product_id, units) values(?,?,?)",
        ("Milk", product_id, 1),
    )
    assert dao.item_exists(1)
    assert not dao.item_exists(2)
    cur.execute(
        "INSERT INTO items(name, product_id, units) values(?,?,?)",
        ("Milk Carton", product_id, 6),
    )
    assert dao.item_exists(1)
    assert dao.item_exists(2)
    assert not dao.item_exists(3)

    assert not dao.receipt_exists(1)
    assert not dao.receipt_exists(2)
    cur.execute("INSERT INTO receipts(status) values(1)")
    assert dao.receipt_exists(1)
    assert not dao.receipt_exists(2)

    assert dao.receipt_status(1)
    assert not dao.receipt_status(2)
    dao.close_receipt(1)
    assert not dao.receipt_status(1)

    receipt_id = dao.open_receipt()
    assert dao.receipt_exists(receipt_id)
    assert dao.receipt_status(receipt_id)
    assert dao.register_item(receipt_id, 1)
    assert dao.register_item(receipt_id, 2)
    assert not dao.register_item(1, 1)
    assert not dao.register_item(1, 2)
    assert not dao.register_item(receipt_id, 3)
    dao.close_receipt(receipt_id)
    assert not dao.register_item(receipt_id, 1)

    receipt = dao.get_receipt(receipt_id)
    assert len(receipt.items) == 2
    assert receipt.grand_total == round(2.95 * 7, 2)
    report = dao.report()
    assert report.revenue == round(2.95 * 7, 2)
    assert report.closed_receipts == 2
    assert report.table == [ReportRow("Milk", 7)]
