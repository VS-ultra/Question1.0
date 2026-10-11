SELECT * FROM users;
INSERT INTO users (first_name, last_name, middle_name, passport_number, phone, birth_date, created_at) VALUES
                                                                                                           ('Александр', 'Попов', 'Андреевич', '4657246316', '+71819600133', '1975-10-01', '2025-10-10 17:27:54'),
                                                                                                           ('Кирилл', 'Лебедев', 'Андреевич', '8359717889', '+74026542351', '2002-07-04', '2024-01-21 07:26:32'),
                                                                                                           ('Екатерина', 'Попова', 'Александровна', '8527662275', '+71618495931', '2005-11-08', '2023-06-19 02:19:48'),
                                                                                                           ('Сергей', 'Петров', 'Павлович', '5554575435', '+75255341928', '1961-04-06', '2024-12-07 04:37:11'),
                                                                                                           ('Анна', 'Новикова', 'Андреевна', '6313983794', '+70305641395', '1994-11-16', '2024-05-14 04:27:09'),
                                                                                                           ('Наталья', 'Попова', 'Сергеевна', '5040881177', '+78849696532', '1975-08-03', '2021-05-27 09:15:05'),
                                                                                                           ('Максим', 'Смирнов', 'Павлович', '2040503457', '+76978480184', '1966-06-04', '2023-07-01 20:57:20'),
                                                                                                           ('Наталья', 'Козлова', 'Александровна', '5315624902', '+72814893252', '1973-09-01', '2026-02-04 20:32:43'),
                                                                                                           ('Светлана', 'Иванова', 'Дмитриевна', '6947972064', '+74303911718', '1999-03-22', '2025-01-16 15:37:21'),
                                                                                                           ('Андрей', 'Новиков', 'Николаевич', '7932322086', '+78346578713', '1993-02-11', '2021-03-07 08:25:41'),
                                                                                                           ('Никита', 'Кузнецов', 'Александрович', '2163842225', '+70310518347', '1994-09-05', '2025-11-10 04:03:33'),
                                                                                                           ('Мария', 'Козлова', 'Павловна', '4119198907', '+71656670106', '1961-06-26', '2021-12-06 11:41:22'),
                                                                                                           ('Сергей', 'Кузнецов', 'Олегович', '8350246991', '+76247317810', '1966-09-27', '2021-02-15 20:25:30'),
                                                                                                           ('Сергей', 'Смирнов', 'Павлович', '8956604740', '+73602606474', '1980-12-24', '2025-09-23 17:05:23'),
                                                                                                           ('Наталья', 'Кузнецова', 'Алексеевна', '4566161324', '+79805009788', '1997-01-17', '2021-09-06 21:32:37'),
                                                                                                           ('Дмитрий', 'Морозов', 'Дмитриевич', '4853523389', '+71939909169', '1971-09-11', '2023-03-22 04:05:58'),
                                                                                                           ('Алексей', 'Кузнецов', 'Алексеевич', '7484237235', '+74751079911', '1973-04-17', '2023-04-04 22:46:34'),
                                                                                                           ('Алексей', 'Петров', 'Андреевич', '7054398830', '+72784980841', '1999-05-04', '2021-11-29 13:10:50'),
                                                                                                           ('Андрей', 'Попов', 'Николаевич', '4450852470', '+75348740164', '2005-01-11', '2022-02-11 10:37:14'),
                                                                                                           ('Наталья', 'Козлова', 'Олеговна', '8007688153', '+70112805982', '1980-03-02', '2023-08-16 17:24:30');
SELECT * FROM users;


SELECT * FROM users WHERE id = 2;
UPDATE users SET phone = '+79990001122' WHERE id = 2;
SELECT * FROM users WHERE id = 2;


INSERT INTO products (name, type, min_amount, max_amount, annual_rate, is_active) VALUES
                                                                                      ('Потребительский кредит Стандарт', 'CREDIT', 50000.00, 1000000.00, 14.90, TRUE),
                                                                                      ('Автокредит Драйв', 'CREDIT', 200000.00, 3000000.00, 11.50, TRUE),
                                                                                      ('Ипотека Семейная', 'CREDIT', 500000.00, 10000000.00, 7.80, TRUE),
                                                                                      ('Бизнес-кредит Старт', 'CREDIT', 300000.00, 5000000.00, 16.20, TRUE),
                                                                                      ('Кредит Экспресс Наличные', 'CREDIT', 10000.00, 300000.00, 22.00, FALSE),
                                                                                      ('Вклад Классический', 'DEPOSIT', 10000.00, 5000000.00, 8.50, TRUE),
                                                                                      ('Вклад Накопительный', 'DEPOSIT', 1000.00, 1000000.00, 9.20, TRUE),
                                                                                      ('Вклад Пенсионный', 'DEPOSIT', 5000.00, 2000000.00, 10.00, TRUE),
                                                                                      ('Вклад Онлайн Плюс', 'DEPOSIT', 50000.00, 10000000.00, 11.10, TRUE),
                                                                                      ('Вклад Сезонный Архив', 'DEPOSIT', 10000.00, 500000.00, 6.50, FALSE);

UPDATE products SET is_active = FALSE WHERE id = 1;
SELECT * FROM products WHERE id = 1 AND type = 'CREDIT';


INSERT INTO requests (user_id, product_id, amount, status, created_at, decided_at) VALUES
                                                                                       (13, 8, 1143952.02, 'NEW', '2023-11-10 00:00:00', NULL),
                                                                                       (5, 6, 1860838.68, 'REJECTED', '2023-12-19 00:00:00', '2023-12-26 00:00:00'),
                                                                                       (8, 1, 569574.60, 'REJECTED', '2024-02-03 00:00:00', '2024-02-05 00:00:00'),
                                                                                       (8, 3, 4059208.70, 'APPROVED', '2024-04-01 00:00:00', '2024-04-08 00:00:00'),
                                                                                       (10, 7, 443279.66, 'APPROVED', '2024-07-08 00:00:00', '2024-07-16 00:00:00'),
                                                                                       (3, 6, 1735537.82, 'APPROVED', '2024-07-10 00:00:00', '2024-07-14 00:00:00'),
                                                                                       (13, 6, 1375364.73, 'APPROVED', '2024-07-19 00:00:00', '2024-07-26 00:00:00'),
                                                                                       (16, 3, 5755074.13, 'APPROVED', '2024-09-15 00:00:00', '2024-09-17 00:00:00'),
                                                                                       (11, 8, 849144.07, 'REJECTED', '2024-09-26 00:00:00', '2024-10-03 00:00:00'),
                                                                                       (19, 7, 311065.60, 'REJECTED', '2024-10-02 00:00:00', '2024-10-03 00:00:00'),
                                                                                       (7, 8, 411446.22, 'REJECTED', '2024-10-13 00:00:00', '2024-10-23 00:00:00'),
                                                                                       (19, 9, 1592087.46, 'APPROVED', '2024-10-25 00:00:00', '2024-10-31 00:00:00'),
                                                                                       (20, 1, 313710.38, 'REJECTED', '2024-10-26 00:00:00', '2024-10-28 00:00:00'),
                                                                                       (6, 3, 3388580.77, 'REJECTED', '2024-10-27 00:00:00', '2024-11-01 00:00:00'),
                                                                                       (16, 9, 2908350.87, 'APPROVED', '2024-12-31 00:00:00', '2025-01-02 00:00:00'),
                                                                                       (17, 1, 466978.63, 'NEW', '2025-01-28 00:00:00', NULL),
                                                                                       (10, 4, 1384439.49, 'NEW', '2025-03-10 00:00:00', NULL),
                                                                                       (6, 4, 1276272.36, 'REJECTED', '2025-05-10 00:00:00', '2025-05-14 00:00:00'),
                                                                                       (1, 1, 550337.23, 'APPROVED', '2025-07-18 00:00:00', '2025-07-25 00:00:00'),
                                                                                       (20, 6, 2730233.64, 'APPROVED', '2025-08-18 00:00:00', '2025-08-21 00:00:00'),
                                                                                       (17, 3, 4000430.59, 'REJECTED', '2025-08-27 00:00:00', '2025-09-03 00:00:00'),
                                                                                       (5, 7, 62730.37, 'APPROVED', '2025-09-11 00:00:00', '2025-09-20 00:00:00'),
                                                                                       (17, 8, 624484.24, 'APPROVED', '2025-10-23 00:00:00', '2025-10-27 00:00:00'),
                                                                                       (7, 6, 1540679.16, 'REJECTED', '2025-10-28 00:00:00', '2025-10-31 00:00:00'),
                                                                                       (16, 8, 576953.22, 'APPROVED', '2025-11-04 00:00:00', '2025-11-08 00:00:00'),
                                                                                       (11, 7, 50707.83, 'APPROVED', '2025-11-25 00:00:00', '2025-11-29 00:00:00'),
                                                                                       (4, 7, 174475.96, 'APPROVED', '2025-12-09 00:00:00', '2025-12-19 00:00:00'),
                                                                                       (10, 4, 1259753.10, 'APPROVED', '2026-02-03 00:00:00', '2026-02-07 00:00:00'),
                                                                                       (15, 3, 3803999.93, 'APPROVED', '2026-03-05 00:00:00', '2026-03-08 00:00:00'),
                                                                                       (19, 8, 101243.06, 'APPROVED', '2026-03-05 00:00:00', '2026-03-09 00:00:00'),
                                                                                       (9, 1, 57833.62, 'APPROVED', '2026-04-22 00:00:00', '2026-04-25 00:00:00'),
                                                                                       (7, 9, 283283.18, 'REJECTED', '2026-04-25 00:00:00', '2026-05-05 00:00:00'),
                                                                                       (18, 1, 293786.62, 'REJECTED', '2026-05-02 00:00:00', '2026-05-12 00:00:00'),
                                                                                       (1, 9, 3923060.11, 'REJECTED', '2026-05-05 00:00:00', '2026-05-07 00:00:00'),
                                                                                       (9, 3, 3256390.56, 'REJECTED', '2026-05-08 00:00:00', '2026-05-14 00:00:00'),
                                                                                       (15, 3, 3875554.95, 'NEW', '2026-05-09 00:00:00', NULL),
                                                                                       (14, 4, 468069.12, 'NEW', '2026-05-16 00:00:00', NULL),
                                                                                       (9, 9, 4971481.11, 'REJECTED', '2026-05-30 00:00:00', '2026-06-02 00:00:00'),
                                                                                       (3, 1, 353075.99, 'REJECTED', '2026-06-01 00:00:00', '2026-06-03 00:00:00'),
                                                                                       (2, 8, 108666.08, 'APPROVED', '2026-06-26 00:00:00', '2026-06-28 00:00:00'),
                                                                                       (2, 9, 3960746.34, 'REJECTED', '2026-07-08 00:00:00', '2026-07-11 00:00:00'),
                                                                                       (9, 2, 1521848.40, 'REJECTED', '2026-08-12 00:00:00', '2026-08-13 00:00:00'),
                                                                                       (18, 1, 530043.08, 'REJECTED', '2026-08-20 00:00:00', '2026-08-23 00:00:00'),
                                                                                       (2, 4, 2477555.35, 'REJECTED', '2026-08-29 00:00:00', '2026-09-08 00:00:00'),
                                                                                       (18, 4, 1971573.35, 'REJECTED', '2026-09-05 00:00:00', '2026-09-11 00:00:00'),
                                                                                       (18, 9, 4716421.23, 'REJECTED', '2026-09-08 00:00:00', '2026-09-09 00:00:00'),
                                                                                       (18, 3, 732093.92, 'REJECTED', '2026-09-10 00:00:00', '2026-09-19 00:00:00'),
                                                                                       (9, 4, 2552326.49, 'REJECTED', '2026-09-20 00:00:00', '2026-09-26 00:00:00'),
                                                                                       (12, 2, 620394.59, 'APPROVED', '2026-09-22 00:00:00', '2026-10-01 00:00:00'),
                                                                                       (2, 7, 595874.30, 'REJECTED', '2026-09-28 00:00:00', '2026-10-01 00:00:00');



INSERT INTO transactions (request_id, type, amount, created_at) VALUES
                                                                    (4, 'DISBURSEMENT', 4059208.70, '2024-04-11 00:00:00'),
                                                                    (5, 'DEPOSIT_TOP_UP', 443279.66, '2024-07-17 00:00:00'),
                                                                    (6, 'DEPOSIT_TOP_UP', 1735537.82, '2024-07-16 00:00:00'),
                                                                    (7, 'DEPOSIT_TOP_UP', 1375364.73, '2024-07-28 00:00:00'),
                                                                    (8, 'DISBURSEMENT', 5755074.13, '2024-09-18 00:00:00'),
                                                                    (12, 'DEPOSIT_TOP_UP', 1592087.46, '2024-11-03 00:00:00'),
                                                                    (15, 'DEPOSIT_TOP_UP', 2908350.87, '2025-01-04 00:00:00'),
                                                                    (19, 'DISBURSEMENT', 550337.23, '2025-07-26 00:00:00'),
                                                                    (20, 'DEPOSIT_TOP_UP', 2730233.64, '2025-08-23 00:00:00'),
                                                                    (20, 'WITHDRAWAL', 372640.27, '2026-05-06 00:00:00'),
                                                                    (22, 'DEPOSIT_TOP_UP', 62730.37, '2025-09-22 00:00:00'),
                                                                    (22, 'WITHDRAWAL', 10801.64, '2025-11-28 00:00:00'),
                                                                    (23, 'DEPOSIT_TOP_UP', 624484.24, '2025-10-28 00:00:00'),
                                                                    (25, 'DEPOSIT_TOP_UP', 576953.22, '2025-11-09 00:00:00'),
                                                                    (26, 'DEPOSIT_TOP_UP', 50707.83, '2025-11-30 00:00:00'),
                                                                    (27, 'DEPOSIT_TOP_UP', 174475.96, '2025-12-22 00:00:00'),
                                                                    (28, 'DISBURSEMENT', 1259753.10, '2026-02-08 00:00:00'),
                                                                    (29, 'DISBURSEMENT', 3803999.93, '2026-03-10 00:00:00'),
                                                                    (30, 'DEPOSIT_TOP_UP', 101243.06, '2026-03-10 00:00:00'),
                                                                    (31, 'DISBURSEMENT', 57833.62, '2026-04-26 00:00:00'),
                                                                    (40, 'DEPOSIT_TOP_UP', 108666.08, '2026-07-01 00:00:00'),
                                                                    (49, 'DISBURSEMENT', 620394.59, '2026-10-03 00:00:00');


DELETE FROM users WHERE id = 2;

SELECT *
FROM requests AS r
JOIN users AS u ON u.id = r.user_id
WHERE u.phone = '+71819600133' AND r.status IN ('NEW', 'APPROVEВ');

SELECT *
FROM transactions AS t
WHERE t.request_id = 1
ORDER BY t.created_at;

SELECT u.id, u.first_name, u.last_name, u.middle_name, SUM(t.amount) as total_disbursed
from transactions as t
JOIN requests AS r ON r.id = t.request_id
JOIN users AS u ON u.id = r.user_id
WHERE t.type = 'DISBURSEMENT'
GROUP BY u.id, u.first_name, u.last_name, u.middle_name
ORDER BY total_disbursed DESC
LIMIT 10;

WITH yearly AS(
    SELECT
        r.user_id,
        SUM(t.amount) FILTER (
            WHERE t.created_at >= date_trunc('year', CURRENT_DATE)
            AND t.created_at <  date_trunc('year', CURRENT_DATE) + INTERVAL '1 year'
            ) AS cur_year,
        SUM(t.amount) FILTER (
            WHERE t.created_at >= date_trunc('year', CURRENT_DATE) - INTERVAL '1 year'
                AND t.created_at <  date_trunc('year', CURRENT_DATE)
            ) AS prev_year
    FROM transactions AS t
    JOIN requests AS r ON r.id = t.request_id
    WHERE t.type = 'DISBURSEMENT'
    GROUP BY r.user_id
)
SELECT u.last_name, u.first_name, u.middle_name, y.cur_year,
       COALESCE(y.prev_year, 0) AS prev_year,
       ROUND((y.cur_year-y.prev_year)/ NULLIF(y.prev_year, 0) * 100, 2) AS growth_pct
FROM yearly AS y
JOIN users AS u ON u.id = y.user_id
WHERE y.cur_year IS NOT NULL
ORDER BY y.cur_year DESC
LIMIT 10;

SELECT p.name, p.min_amount, p.max_amount, p.annual_rate
FROM products AS p
WHERE p.is_active = TRUE;

SELECT p.name, COUNT(DISTINCT (r.id)) AS submitted
FROM products AS p
LEFT JOIN requests as r on r.product_id = p.id
WHERE p.is_active = TRUE
GROUP BY p.id, p.name;

SELECT p.name,
       ROUND(
               100.0 * COUNT(DISTINCT r.id) FILTER (WHERE r.status = 'APPROVED')
                   / NULLIF(COUNT(DISTINCT r.id), 0),
               2) AS approved_percent
FROM products AS p
LEFT JOIN requests AS r ON r.product_id = p.id
WHERE p.is_active = TRUE
GROUP BY p.id, p.name;


SELECT p.name, COUNT(t.request_id) as disbursed
FROM products AS p
LEFT JOIN requests as r on r.product_id = p.id
LEFT JOIN transactions AS t ON t.request_id = r.id AND t.type = 'DISBURSEMENT'
WHERE p.is_active = TRUE
GROUP BY p.id, p.name;


SELECT p.name, COALESCE(SUM(t.amount), 0) AS total_disbursed
FROM products AS p
LEFT JOIN requests as r on r.product_id = p.id
LEFT JOIN transactions AS t ON t.request_id = r.id AND t.type = 'DISBURSEMENT'
WHERE p.is_active = TRUE
GROUP BY p.id, p.name;

SELECT
    TO_CHAR(DATE_TRUNC('month', r.created_at), 'TMMonth YYYY') AS month_year,
    COUNT(DISTINCT r.id)          AS submitted,
    COUNT(DISTINCT t.request_id)  AS disbursed
FROM requests AS r
         LEFT JOIN transactions AS t
                   ON t.request_id = r.id AND t.type = 'DISBURSEMENT'
WHERE r.created_at >= DATE_TRUNC('month', LOCALTIMESTAMP) - INTERVAL '35 months'
GROUP BY DATE_TRUNC('month', r.created_at)
ORDER BY DATE_TRUNC('month', r.created_at);

WITH request_age AS (
    SELECT
        r.status,
        DATE_PART('year', AGE(r.created_at, u.birth_date)) AS age
    FROM requests AS r
    JOIN users AS u ON u.id = r.user_id
    WHERE r.status != 'NEW'
)
SELECT
    CASE
        WHEN age BETWEEN 18 AND 25 THEN '18-25'
        WHEN age BETWEEN 26 AND 35 THEN '26-35'
        WHEN age BETWEEN 36 AND 50 THEN '36-50'
        ELSE '51+'
    END AS age_group,
    COUNT(*) AS total,
    COUNT(*) FILTER ( WHERE status = 'APPROVED' ) AS approved,
    ROUND(100.0 * COUNT(*) FILTER ( WHERE status = 'APPROVED' ) / COUNT(*),2) AS approvable
FROM request_age
WHERE age >=18
GROUP BY 1
ORDER BY MIN(age);

SELECT
    u.last_name, u.first_name, u.middle_name, u.phone,
    COUNT(*) AS submitted,
    COUNT(*) FILTER (WHERE r.status = 'APPROVED') AS approved,
    ROUND(100.0 * COUNT(*) FILTER (WHERE r.status = 'APPROVED') / COUNT(*), 2) AS approval_pct
FROM users AS u
         JOIN requests AS r ON r.user_id = u.id
WHERE r.created_at >= LOCALTIMESTAMP - INTERVAL '6 months'
GROUP BY u.id
HAVING COUNT(*) >= 3
   AND 100.0 * COUNT(*) FILTER (WHERE r.status = 'APPROVED') / COUNT(*) < 30
ORDER BY approval_pct, submitted DESC;