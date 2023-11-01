(define (reverse_ lst)
    (if (null? lst) '()
        (append (reverse_ (cdr lst)) (list (car lst)))
    )
)

(define (sorted? lst)
    (if (<= (length lst) 1) #t
        (if (> (car lst) (car (cdr lst))) #f
            (sorted? (cdr lst))
        )
    )
)

(define (max_ lst)
    (if (null? lst) 0
        (if (< (car lst) (max_ (cdr lst))) (max_ (cdr lst))
            (car lst)
        )
    )
)

(define (filter_ func lst)
    (if (null? lst) '()
        (if (func (car lst)) (cons (car lst) (filter_ func (cdr lst)))
            (filter_ func (cdr lst))
        )
    )
)

(define (range k)
    (if (zero? k) 
        '()
        (append (range (- k 1)) (list k))
    )
)

(define (pow a n)
    (if (zero? n)
        1
        (* a (pow a (- n 1)))
    )
)

(define (generate_powers_helper a k curr_pow)
    (if (zero? k) '()
        (cons curr_pow (generate_powers_helper a (- k 1) (* a curr_pow)))
    )
)

(define (generate_powers a k)
    (generate_powers_helper a k a)
)

(display (if (equal? (reverse_ '()) '()) "1 SUCCESS" "1 FAILURE"))
(display (if (equal? (reverse_ '(1 2 3)) '(3 2 1)) "2 SUCCESS" "2 FAILURE"))

(display (if (equal? (sorted? '(1 2 3)) #t) "3 SUCCESS" "3 FAILURE"))
(display (if (equal? (sorted? '(5 3 6)) #f) "4 SUCCESS" "4 FAILURE"))


(display (if (equal? (max_ '()) 0) "5 SUCCESS" "5 FAILURE"))
(display (if (equal? (max_ '(4 5 3)) 5) "6 SUCCESS" "6 FAILURE"))

(display (if (equal? (range 5) '(1 2 3 4 5)) "7 SUCCESS" "7 FAILURE"))

(display (if (equal? (pow 2 5) 32) "8 SUCCESS" "8 FAILURE"))
(display (if (equal? (pow 7 1) 7) "9 SUCCESS" "9 FAILURE"))

(display (if (equal? (generate_powers 2 4) '(2 4 8 16)) "10 SUCCESS" "10 FAILURE"))