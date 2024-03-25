-- MERGE INTO films (id, name, description, releaseDate, duration, mp_id)
--     VALUES (1, '1-NAME', '1-DESCRIPTION', '1967-03-25', 10, 1),
--            (2, '2-NAME', '2-DESCRIPTION', '1967-03-25', 10, 1),
--            (3, '3-NAME', '3-DESCRIPTION', '1967-03-25', 10, 1),
--            (4, '4-NAME', '4-DESCRIPTION', '1967-03-25', 10, 1),
--            (5, '5-NAME', '5-DESCRIPTION', '1967-03-25', 10, 1),
--            (6, '6-NAME', '6-DESCRIPTION', '1967-03-25', 10, 1),
--            (7, '7-NAME', '7-DESCRIPTION', '1967-03-25', 10, 1),
--            (8, '8-NAME', '8-DESCRIPTION', '1967-03-25', 10, 1),
--            (9, '9-NAME', '9-DESCRIPTION', '1967-03-25', 10, 1),
--            (10, '10-NAME', '10-DESCRIPTION', '1967-03-25', 10, 1),
--            (11, '11-NAME', '11-DESCRIPTION', '1967-03-25', 10, 1),
--            (12, '12-NAME', '12-DESCRIPTION', '1967-03-25', 10, 1);
--
-- MERGE INTO users (id, mail, login, name, bithday)
--     VALUES (1, '1-EMAIL@mail.ru', '1-LOGIN', '1-NAME', '1967-03-25'),
--            (2, '2-EMAIL@mail.ru', '2-LOGIN', '2-NAME', '1967-03-25'),
--            (3, '3-EMAIL@mail.ru', '3-LOGIN', '3-NAME', '1967-03-25'),
--            (4, '4-EMAIL@mail.ru', '4-LOGIN', '4-NAME', '1967-03-25'),
--            (5, '5-EMAIL@mail.ru', '5-LOGIN', '5-NAME', '1967-03-25'),
--            (6, '6-EMAIL@mail.ru', '6-LOGIN', '6-NAME', '1967-03-25'),
--            (7, '7-EMAIL@mail.ru', '7-LOGIN', '7-NAME', '1967-03-25'),
--            (8, '8-EMAIL@mail.ru', '8-LOGIN', '8-NAME', '1967-03-25');
--
-- MERGE INTO likes (film_id, user_id)
--     VALUES (1, 1),
--            (1, 2),
--            (1, 3),
--            (1, 4),
--            (1, 5),
--            (1, 6),
--            (1, 7),
--            (2, 1),
--            (2, 2),
--            (2, 3),
--            (2, 4),
--            (2, 5),
--            (2, 6),
--            (2, 7),
--            (3, 1),
--            (3, 2),
--            (3, 3),
--            (3, 4),
--            (4, 1),
--            (4, 2),
--            (4, 3),
--            (4, 4),
--            (5, 1),
--            (5, 2),
--            (5, 3),
--            (5, 4),
--            (6, 1),
--            (6, 2),
--            (6, 3),
--            (6, 4),
--            (7, 1),
--            (7, 2),
--            (7, 3),
--            (7, 4),
--            (8, 1),
--            (8, 2),
--            (8, 3),
--            (8, 4),
--            (9, 1),
--            (9, 2),
--            (9, 3),
--            (9, 4),
--            (9, 5),
--            (9, 6),
--            (9, 7),
--            (9, 8),
--            (11, 1),
--            (11, 2),
--            (11, 3),
--            (11, 4),
--            (12, 1),
--            (12, 2),
--            (12, 3),
--            (10, 1),
--            (10, 2),
--            (10, 3);
--
-- INSERT INTO films_genre (film_id, genre_id)
-- VALUES (1, 1),
--        (1, 2),
--        (2, 2),
--        (2, 3),
--        (2, 4),
--        (3, 1),
--        (3, 2),
--        (3, 4),
--        (4, 5);
--
-- MERGE INTO friends (user_id, friend, match)
--     VALUES (1, 2, true),
--            (2, 3, false),
--            (3, 1, true),
--            (4, 1, false),
--            (5, 2, true),
--            (6, 2, true),
--            (7, 4, true),
--            (1, 7, true),
--            (3, 6, true),
--            (5, 6, true),
--            (6, 7, true);
-- SELECT F.*, M.name, G.id, G.name
-- FROM films AS F
--          LEFT JOIN PUBLIC.MPA M on M.ID = F.MPA_ID
--          LEFT JOIN PUBLIC.FILMS_GENRES FG on F.ID = FG.FILM_ID
--          LEFT JOIN PUBLIC.GENRES G on G.ID = FG.GENRE_ID
-- WHERE F.ID = 2;
--
-- INSERT INTO FILMS_USERS (FILM_ID, USER_ID)
-- VALUES (5, 8);


SELECT f.*,
       m.NAME,
       GROUP_CONCAT(G2.GENRE_ID) as genre_id,
       GROUP_CONCAT(G2.NAME)     as genre_name

FROM FILMS f
         LEFT JOIN FILM_GENRE FG on f.FILM_ID = FG.FILM_ID
         LEFT JOIN MPA M on M.MPA_ID = f.MPA_ID
         LEFT JOIN GENRES G2 on FG.GENRE_ID = G2.GENRE_ID
GROUP BY f.FILM_ID;



SELECT f.*,
       M.NAME,
       (SELECT GROUP_CONCAT(GENRE_ID)
        FROM GENRES
        WHERE FILM_ID = f.FILM_ID)         as genre_id,
       (SELECT GROUP_CONCAT(g.NAME)
        FROM GENRES as g
        WHERE GENRE_ID IN
              (SELECT g.GENRE_ID
               FROM FILM_GENRE as g
               WHERE g.FILM_ID = f.FILM_ID)) as name
FROM FILMS as f
         LEFT JOIN MPA M on f.MPA_ID = M.MPA_ID
         LEFT JOIN FILMS_LIKES L on f.FILM_ID = L.FILM_ID
GROUP BY f.FILM_ID
ORDER BY count(l.USER_ID) DESC, f.FILM_ID
LIMIT ?;




SELECT f.*,
       M.NAME,
       (SELECT GROUP_CONCAT(GENRE_ID)
        FROM FILM_GENRE
        WHERE FILM_ID = f.FILM_ID)         as genre_id,
       (SELECT GROUP_CONCAT(g.NAME)
        FROM GENRES as g
        WHERE GENRE_ID IN
              (SELECT g.GENRE_ID
               FROM FILM_GENRE as g
               WHERE FILM_ID = f.FILM_ID)) as name
FROM FILMS as f
         LEFT JOIN MPA M on f.MPA_ID = M.MPA_ID
         LEFT JOIN FILMS_LIKES L on f.FILM_ID = L.FILM_ID
GROUP BY f.FILM_ID
ORDER BY count(l.USER_ID) DESC, f.FILM_ID
LIMIT ?;




