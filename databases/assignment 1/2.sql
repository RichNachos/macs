/*
2)  (6 ქულა)
დაწერეთ query, რომელიც გამოიტანს ყველა იმ დეპარამენტის ინფორმაციას, რომელთაც:
• არ ყავთ მენეჯერი, ან თუ ყავთ, მაშინ ამ მენეჯერის დასაქმებიდან გასული უნდა იყოს 6 თვე მაინც.
• ამ დეპარტამენტებში მომუშავე არცერთი თანამშრომლის პოზიცია არ არის და არც წარსულში ყოფილა  არც გაყიდვების მენეჯერი (SA_MAN) და არც პროგრამისტი (IT_PROG).
დაბეჭდეთ ველები:
• დეპარტმანენტის დასახელება
• მენეჯერის სახელი და გვარი გადაბმული ერთი ცარიელი სიმბოლოთი
• მენეჯერის ხელფასი ფორმატით $12,456.00
• დეპარტამენტის მისამართი (ქვეყნის დასახელება, ქალაქი, ქუჩა. მაგ: Italy, Roma, 1297 Via Cola di Rie)
*/

SELECT *
       FROM departments d
       LEFT JOIN employees e
            ON d.manager_id = e.employee_id
       JOIN locations l
            ON d.location_id = l.location_id
       JOIN countries c
            ON l.country_id = c.country_id
       WHERE
            ((d.manager_id IS NULL) OR MONTHS_BETWEEN(sysdate, e.hire_date) >= 6)
         AND 0 = (SELECT count(ee.employee_id)
                         FROM employees ee
                         LEFT JOIN job_history hh
                              ON ee.employee_id = hh.employee_id
                         WHERE
                              d.department_id = ee.department_id
                          AND (ee.job_id IN ('SA_MAN', 'IT_PROG')
                            OR hh.job_id IN ('SA_MAN', 'IT_PROG')))

