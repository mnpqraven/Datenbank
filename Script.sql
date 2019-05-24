--auf 1
SELECT Bestellungen.*
FROM  Bestellungen NATURAL JOIN Kunden
WHERE username = 'user42';

--auf 2
SELECT titel
FROM (Filme NATURAL JOIN Bestellposten) NATURAL JOIN (Bestellungen NATURAL JOIN Kunden)
WHERE username = 'user42';

--auf 3
SELECT titel
FROM ((Filme NATURAL JOIN Kategorien) NATURAL JOIN Bestellposten) NATURAL JOIN Bestellungen
WHERE kunden_id = '7747' AND Kategorien.name = 'Sci-Fi';

--auf 4
SELECT vorname, nachname
FROM Kunden
WHERE land = 'Germany' AND kunden_id NOT IN (SELECT kunden_id FROM Bestellungen NATURAL JOIN Kunden)
ORDER BY nachname asc;

--auf 5
SELECT kunden.land
FROM Kunden
GROUP BY kunden.land
ORDER BY count(kunden_id) LIMIT 1;

--auf 6
SELECT (sum(betrag)) as umsatz
FROM Bestellungen
WHERE datum BETWEEN date '2009-01-01' AND date '2009-1-31'

--auf 7
SELECT sum(CASE
            WHEN kundenalter BETWEEN 16 AND 25 THEN betrag *0.75
            ELSE betrag
           END)
     - sum(betrag)
FROM Bestellungen NATURAL JOIN Kunden
WHERE datum BETWEEN date '2009-08-01' AND date '2009-08-31';

--auf 8
SELECT name, sum(views_anz)
FROM Filme NATURAL JOIN Kategorien NATURAL JOIN Bestellposten
GROUP BY name ORDER BY sum(views_anz) desc LIMIT 1;

--auf 9
SELECT titel, count(bestell_id)
FROM Filme NATURAL JOIN Bestellposten
WHERE datum BETWEEN date '2009-01-01' AND date '2009-01-31'
GROUP BY titel
ORDER BY count(bestell_id) DESC, titel ASC LIMIT 5;

--auf 10
SELECT EXTRACT(month from S.datum) as month, sum(S.betrag) as each_umsatz
FROM (SELECT * FROM Bestellungen WHERE datum BETWEEN date '2009-01-01' and date '2009-12-31') S
GROUP BY month
HAVING sum(S.betrag) > (SELECT sum(betrag)/12 FROM Bestellungen WHERE datum BETWEEN date '2009-01-01' AND date '2009-12-31')
ORDER BY month;

--auf 11
(SELECT DISTINCT titel, film_id
FROM Filme NATURAL JOIN Kategorien NATURAL JOIN Bestellposten NATURAL JOIN Bestellungen NATURAL JOIN Kunden
WHERE Kategorien.kategorie = 4)
EXCEPT
(SELECT DISTINCT titel, film_id
FROM Filme NATURAL JOIN Bestellungen NATURAL JOIN Bestellposten NATURAL JOIN Kunden
WHERE geschlecht != 'F'
)
ORDER BY film_id;

--auf 12
SELECT count(DISTINCT kunden_id)
FROM Kategorien NATURAL JOIN Filme NATURAL JOIN Bestellposten NATURAL JOIN Bestellungen NATURAL JOIN Kunden
WHERE kundenalter > 60 AND (name = 'Horror' OR oberkategorie IN (SELECT kategorie FROM Kategorien WHERE name = 'Horror'));

--auf 13
SELECT count(*)
FROM(
    SELECT DISTINCT kunden_id
    FROM Filme NATURAL JOIN Bestellposten NATURAL JOIN Bestellungen
    WHERE kategorie IN (1,2,3,4,5,6,7,8)
    GROUP BY kunden_id
    HAVING count(DISTINCT kategorie) = 8
    ) S;
