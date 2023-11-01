/*
3)  (8 ქულა)
ავიაბილეთების ონლაინ გაყიდვის სისტემისთვის ააგეთ ცხრილების სტრუქრურა:

გვაქვს ავიაკომპანიები, რომელთაც აქვთ რეისები სხვადასხვა მიმართულებით სხვადასხვა თარიღში.
ამ რეისების ყიდვა უნდა შეეძლოს მომხმარებელს.

სტრუქტურის ასაგებად მინიმუმ დაგჭირდებათ შემდეგი ცხრილების გაკეთება (ცხრილებში საჭირო ველები თქვენ განსაზღვრეთ):
1. ავიაკომპანიები;
2. ქალაქები (საიდან და სად რეისისთვის);
3. რეისები;
4. მომხმარებლები;
5. მომხმარებლის მიერ შეძენილი ბილეთები;

ცხრილები ატრიბუტები(ველები, გადაბმები, შეზღუდვები) თქვენით შეგიძლიათ მოიფიქრო ისე, რომ დიზაინი იყოს ოპტიმალური.
თუ საჭიროდ ჩათვლისთ შეგიძლიათ დაამატოთ სხვა ცხრილებიც.

შექმენით view, რომელიც რეისების ჭრილში წამოიღებს შემდეგ ინფორმაციას:
• ავიაკომპანიის სახელი; 
• რეისის ნომერი; 
• "საიდან" და "სად" ქალაქის სახელი; 
• ფრენის დრო; 
• გაყიდული ბილეთების რაოდენობა; 
• ბილეთების გაყიდვიდან შემოსული ჯამური თანხა; 
• ფრენის დღეს იგივე მიმართულებით (საიდან - სად) სხვა რეისების რაოდენობა.
*/

/*    CREATE VIEW CODE       */
CREATE OR REPLACE VIEW flight_view AS
       SELECT  a.airline_name            AIRLINE_NAME
              ,f.flight_id               FLIGHT_ID
              ,dl.city_name              DEPARTURE_CITY
              ,al.city_name              ARRIVAL_CITY
              ,f.departure_datetime      DEPARTURE_TIME
              ,(SELECT COUNT(tt.ticket_id)
                     FROM tickets tt
                     WHERE tt.flight_id = f.flight_id)        TICKET_COUNT
              ,(SELECT SUM(tt.fare_price) 
                       FROM tickets tt
                       WHERE tt.flight_id = f.flight_id)      TICKET_INCOME
              ,(SELECT count(ff.flight_id)
                       FROM flights ff
                       WHERE 
                            TO_DATE(sysdate,'DD-MM-YYYY') - TO_DATE('19-12-2021','DD-MM-YYYY') = 0
                        AND f.departure_location_id = ff.departure_location_id
                        AND f.arrival_location_id = ff.arrival_location_id)         OTHER_FLIGHTS_COUNT
                        
              FROM flights f
              JOIN airlines a
                   ON f.airline_id = a.airline_id
              JOIN locations_airports dl
                   ON f.departure_location_id = dl.location_id
              JOIN locations_airports al
                   ON f.arrival_location_id = al.location_id
              GROUP BY f.flight_id, a.airline_name, dl.city_name, al.city_name, f.departure_datetime, f.departure_location_id, f.arrival_location_id


/*    CREATE TABLES CODE     */
/*
AIRLINES
AIRLINE_ID, AIRLINE_NAME, EMAIL
*/
CREATE TABLE airlines
       ( airline_id   NUMBER(10)
           CONSTRAINT   air_airline_id_pk   PRIMARY KEY
        ,airline_name VARCHAR2(50)
           CONSTRAINT   air_airline_name_nn NOT NULL
           CONSTRAINT   air_airline_name_uk UNIQUE
        ,email        VARCHAR2(60)
           CONSTRAINT   air_email_nn        NOT NULL
           CONSTRAINT   air_email_uk        UNIQUE         )
/*
LOCATIONS
LOCATION_ID, AIRPORT_NAME, CITY_NAME, ADDRESS, STATE_PROVINCE, ZIP_CODE
*/
CREATE TABLE locations_airports
       ( location_id       NUMBER(10)
           CONSTRAINT loc_location_id_pk       PRIMARY KEY
        ,airport_name      VARCHAR2(60)
           CONSTRAINT loc_airport_name_nn      NOT NULL
           CONSTRAINT loc_airport_name_uk      UNIQUE
        ,city_name         VARCHAR2(60)
           CONSTRAINT loc_city_name_nn         NOT NULL
        ,address           VARCHAR2(120)
           CONSTRAINT loc_address_nn           NOT NULL
           CONSTRAINT loc_address_uk           UNIQUE
        ,state_province    VARCHAR2(60)
        ,zip_code          NUMBER(10)
           CONSTRAINT loc_zip_code_nn          NOT NULL
           CONSTRAINT loc_zip_code_uk           UNIQUE       )

/*
FLIGHTS
FLIGHT_ID, AIRLINE_ID, DEPARTURE_LOCATION_ID, ARRIVAL_LOCATION_ID, DEPARTURE_DATETIME, ARRIVAL_DATETIME
*/
CREATE TABLE flights
       ( flight_id                NUMBER(10)
           CONSTRAINT fli_flight_id_pk                 PRIMARY KEY
        ,airline_id               NUMBER(10)
           CONSTRAINT fli_airline_id_nn                NOT NULL
           CONSTRAINT fli_airline_id_fk                REFERENCES
              airlines (airline_id)
        ,departure_location_id    NUMBER(10)
           CONSTRAINT fli_departure_location_id_nn     NOT NULL
           CONSTRAINT fli_departure_location_id_fk     REFERENCES
              locations_airports (location_id)
        ,arrival_location_id      NUMBER(10)
           CONSTRAINT fli_arrival_location_id_nn       NOT NULL
           CONSTRAINT fli_arrival_location_id_fk       REFERENCES
              locations_airports (location_id)
        ,departure_datetime       TIMESTAMP
           CONSTRAINT fli_departure_datetime_nn        NOT NULL
        ,arrival_datetime         TIMESTAMP
           CONSTRAINT fli_arrival_datetime_nn          NOT NULL    )
           
/*
CUSTOMERS
CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, PERSONAL_ID
*/
CREATE TABLE customers
       ( customer_id  NUMBER(10)
           CONSTRAINT cus_customer_id_pk           PRIMARY KEY
        ,first_name   VARCHAR2(50)
           CONSTRAINT cus_first_name_nn            NOT NULL
        ,last_name    VARCHAR2(50)
           CONSTRAINT cus_last_name_nn             NOT NULL
        ,email        VARCHAR2(50)
           CONSTRAINT cus_email_nn                 NOT NULL
           CONSTRAINT cus_email_uk                 UNIQUE
        ,phone_number VARCHAR2(20)
           CONSTRAINT cus_phone_number_nn          NOT NULL
           CONSTRAINT cus_phone_number_uk          UNIQUE
        ,personal_id  VARCHAR2(30)
           CONSTRAINT cus_personal_id_nn           NOT NULL
           CONSTRAINT cus_personal_id_uk           UNIQUE          )
           
/*
TICKETS
TICKET_ID, FLIGHT_ID, CUSTOMER_ID, PURCHASE_DATETIME, FARE_PRICE
*/
CREATE TABLE tickets
       ( ticket_id          NUMBER(10)
           CONSTRAINT tic_ticket_id_pk                PRIMARY KEY
        ,flight_id          NUMBER(10)
           CONSTRAINT tic_flight_id_fk                REFERENCES
             flights (flight_id)
        ,customer_id        NUMBER(10)
           CONSTRAINT tic_customer_id_fk              REFERENCES
             customers (customer_id)
        ,purchase_datetime  TIMESTAMP
           CONSTRAINT tic_purchase_datetime_nn        NOT NULL
        ,fare_price         NUMBER(10, 2)
           CONSTRAINT tic_fare_price_ck               CHECK (fare_price > 0)           )

