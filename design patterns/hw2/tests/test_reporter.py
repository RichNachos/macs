from app.classes.reporter import Reporter


def test_reporter() -> None:
    table = [
        ("Milk", 1, 4.00, 4.00),
        ("Coca Cola", 1, 1.50, 1.50),
    ]
    reporter = Reporter()
    data = reporter.make_receipt(table, 5.50)
    assert data == (
        "| Name      |   Units |   Price |   Total |\n"
        + "|-----------+---------+---------+---------|\n"
        + "| Milk      |       1 |    4.00 |    4.00 |\n"
        + "| Coca Cola |       1 |    1.50 |    1.50 |\n"
        + "\nSum: 5.50\n"
    )

    new_table = [
        ("Milk", 5),
        ("Coca Cola", 10),
    ]
    data = reporter.make_x_report(new_table, 35.00)
    assert data == (
        "| Name      |   Sold |\n"
        + "|-----------+--------|\n"
        + "| Milk      |      5 |\n"
        + "| Coca Cola |     10 |\n"
        + "\nTotal Revenue: 35.00\n"
    )
