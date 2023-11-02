from app.classes.product import Product


def test_product() -> None:
    product_name = "Coca Cola"
    product1 = Product(product_name, 1.00)
    product2 = Product(product_name, 2.00)
    assert not product1.equals(product2)
    product2 = Product(product_name, 1.00)
    assert product1.equals(product2)
    product2 = Product("Fanta", 1.00)
    assert not product1.equals(product2)
