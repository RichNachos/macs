CREATE OR REPLACE PACKAGE std_package IS
  FUNCTION AddUser
    (f_name_in IN std_users.first_name%TYPE,
     l_name_in IN std_users.last_name%TYPE,
     personal_number_in IN std_users.personal_number%TYPE,
     phone_number_in IN std_users.phone%TYPE,
     email_in IN std_users.email%TYPE)
     RETURN number;
  
  PROCEDURE AddProduct
       (p_model_in IN std_products.product_model%TYPE,
        p_info_in IN std_products.product_info%TYPE,
        p_price_in IN std_products.price%TYPE,
        p_quantity_in IN std_products.product_quantity%TYPE);
 
  FUNCTION FindProductQuantity
    (p_model_in IN std_products.product_model%TYPE)
    RETURN number;
  
  PROCEDURE BuyProduct
    (u_id_in IN std_users.id%TYPE,
     p_id_in IN std_products.id%TYPE,
     result_code OUT varchar2);
  
END std_package;
/
CREATE OR REPLACE PACKAGE BODY std_package IS
  
  FUNCTION AddUser
         (f_name_in IN std_users.first_name%TYPE,
          l_name_in IN std_users.last_name%TYPE,
          personal_number_in IN std_users.personal_number%TYPE,
          phone_number_in IN std_users.phone%TYPE,
          email_in IN std_users.email%TYPE)
         RETURN number
  IS
         cid number;
         new_id number;
         CURSOR c1 IS
                SELECT u.id
                       FROM std_users u
                       WHERE u.personal_number = personal_number_in
                          OR u.phone = phone_number_in
                          OR u.email = email_in;

  BEGIN
         OPEN c1;
         FETCH c1 INTO cid;
         
         IF c1%notfound THEN
           new_id := std_misc_id_seq.nextval;
           INSERT INTO std_users(id, first_name, last_name, personal_number, phone, email, status, registration_date)
                       VALUES(new_id, f_name_in, l_name_in, personal_number_in, phone_number_in, email_in, 'A', sysdate);
           DBMS_OUTPUT.PUT_LINE('Successfully registered new user with ID: ' || new_id);
         ELSE
           RAISE_APPLICATION_ERROR(-20000, 'Registered user found with same information');
           new_id := -1;
         END IF;
         
         CLOSE c1;
  RETURN new_id;
  END;
    
  PROCEDURE AddProduct
         (p_model_in IN std_products.product_model%TYPE,
          p_info_in IN std_products.product_info%TYPE,
          p_price_in IN std_products.price%TYPE,
          p_quantity_in IN std_products.product_quantity%TYPE)
  IS
  BEGIN
    INSERT INTO std_products(id,product_model,product_info,price,status,product_quantity)
                VALUES (std_misc_id_seq.nextval, p_model_in, p_info_in, p_price_in, 'A', p_quantity_in);
  END;
  
  FUNCTION FindProductQuantity
    (p_model_in IN std_products.product_model%TYPE)
    RETURN number
  IS
    ret_val number;
  BEGIN
    BEGIN
      SELECT p.product_quantity INTO ret_val FROM std_products p WHERE p.product_model = p_model_in;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          ret_val := NULL;
    END;
  RETURN ret_val;
  END;
  
  PROCEDURE BuyProduct
    (u_id_in IN std_users.id%TYPE,
     p_id_in IN std_products.id%TYPE,
     result_code OUT varchar2)
  IS
    v_usr number;
    v_prd number;
    v_prd_qnt number;
    v_ord_id number;
  BEGIN
    SELECT COUNT(u.id) INTO v_usr FROM std_users u WHERE u.id = u_id_in;
    SELECT COUNT(p.id) INTO v_prd FROM std_products p WHERE p.id = p_id_in;
    
    IF (v_usr = 0) THEN
      result_code := 'USER_NOT_FOUND';
      RETURN;
      RAISE_APPLICATION_ERROR(-20010, 'No user found with ID ' || u_id_in);
    ELSIF (v_prd = 0) THEN
      result_code := 'PRODUCT_NOT_FOUND';
      RETURN;
      RAISE_APPLICATION_ERROR(-20020, 'No product found with ID ' || u_id_in);
    END IF;
    
    SELECT p.product_quantity INTO v_prd_qnt FROM std_products p WHERE p.id = p_id_in;
    IF (v_prd_qnt = 0) THEN
      result_code := 'PRODUCT_OUT_OF_STOCK';
      RETURN;
      RAISE_APPLICATION_ERROR(-20030, 'Product with ID ' || p_id_in || ' is out of stock');
    END IF;
    
    UPDATE std_products p
      SET p.product_quantity = p.product_quantity - 1
      WHERE p.id = p_id_in;
    
    v_ord_id := std_misc_id_seq.nextval;
    INSERT INTO std_user_orders(id,user_id,product_id,order_status,order_date)
                VALUES(v_ord_id,u_id_in,p_id_in,'PENDING',sysdate);
    result_code := 'SUCCESS';
  END;
END;
/
