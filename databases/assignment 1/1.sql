

/*
1)  (6 ქულა)
დაწერეთ query, რომელიც წამოიღებს იმ მენეჯერების ინფორმაციას, რომლებიც: 
• არიან მინიმუმ 3 თანამშრომლის მენეჯერი.
• მათი ხელფასი აღემატება მათივე დეპარტამენტის საშუალო ხელფასს.
დაბეჭდეთ ველები:
• მენეჯერის სახელი და გვარი
• მენეჯერის email ის @ მდე მოჭრილიმნიშვნელობა (email ის არარსებობის შემთხვევაში დაბეჭდეთ - )
• მენეჯერის დასაქმების თარიღი ფორმატით 05-JAN-2011
• მანეჯერის ქვეშ არსებული თანამშრომლების რაოდენობა
• მანეჯერის ქვეშ არსებული თანამშრომლების საშუალო ხელფასი
• მანეჯერის ქვეშ არსებული გასხვავებული პოზიციების რაოდენობა
მონაცემები დაალაგეთ ისე, რომ პირველი წამოვიდეს 120 ID ის მქონე მენეჯერი, ბოლო წამოვიდეს 108 ID ის მქონი მენეჯერი,
ხოლო დანარჩენები მოთავსებული იყოს მათ შორის და დალაგებული იყოს თანამშრომლების რაოდენობის კლებადობით.
*/


SELECT e.first_name
       ,e.last_name
       ,CASE WHEN INSTR(e.email, '@') > 0 THEN COALESCE(SUBSTR(e.email, 1, INSTR(e.email, '@') - 1), '-') ELSE e.email END EMAIL
       ,TO_CHAR(e.hire_date, 'DD-MON-YYYY') HIRE_DATE
       ,COUNT(ee.employee_id) EMPLOYEE_COUNT
       ,AVG(ee.salary) AVG_EMPLOYEE_SALARY
       ,COUNT(DISTINCT ee.job_id) DIST_POS
       
       FROM employees e
       INNER JOIN employees ee 
            ON e.employee_id = ee.manager_id
       WHERE e.salary > (SELECT avg(ee.salary)
                                FROM employees ee
                                WHERE e.department_id = ee.department_id)
       GROUP BY e.employee_id, e.first_name, e.last_name, e.hire_date, e.email
       HAVING count(ee.employee_id) >= 3
       ORDER BY DECODE(e.employee_id, 120, 1, 108, 3, 2) ASC, COUNT(ee.employee_id) DESC
