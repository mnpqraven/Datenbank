-- Aufgabe 1
SELECT Bestellungen.*
FROM Bestellungen NATURAL JOIN Kunden
WHERE username = 'user42';

--Aufgabe 2
SELECT titel
FROM (Filme NATURAL JOIN Bestellposten) NATURAL JOIN (Bestellungen NATURAL JOIN Kunden)
WHERE username = 'user42';

--Aufgabe 3
SELECT titel
FROM ((Filme NATURAL JOIN Kategorien) NATURAL JOIN Bestellposten) NATURAL JOIN Bestellungen
WHERE kunden_id = '7747' AND Kategorien.name = 'Sci-Fi';

--Aufgabe 4
SELECT vorname, nachname
FROM Kunden
WHERE land = 'Germany' AND kunden_id NOT IN (SELECT kunden_id FROM Bestellungen NATURAL JOIN Kunden)
ORDER BY nachname asc;

--Aufgabe 5
SELECT land
FROM Kunden 
GROUP BY land
ORDER BY count(kunden_id)
LIMIT 1;

--Aufgabe 6
SELECT sum(betrag)
FROM Bestellungen
WHERE datum BETWEEN date '2009-01-01' AND date '2009-01-31';

--Aufgabe 7

SELECT sum(CASE WHEN kundenalter BETWEEN 16 AND 25 THEN betrag*0.75 ELSE betrag END) - sum(betrag)
FROM Bestellungen NATURAL JOIN Kunden
WHERE datum BETWEEN date '2009-08-01' AND date '2009-08-31';

--Aufgabe 8
SELECT name, sum(views_anz)
FROM (Kategorien NATURAL JOIN Filme) NATURAL JOIN Bestellposten
GROUP BY name
ORDER BY sum(views_anz) desc
LIMIT 1;

--Aufgabe 9
SELECT titel, count(bestell_id) AS Bestellanzahl
FROM (Filme NATURAL JOIN Bestellposten) NATURAL JOIN Bestellungen
WHERE datum BETWEEN date '2009-01-01' AND date '2009-01-31'
GROUP BY titel
ORDER BY Bestellanzahl desc, titel asc
LIMIT 5;

--Aufgabe 10
SELECT sum(S.betrag), extract(month from S.datum) AS month  
FROM (SELECT * FROM Bestellungen WHERE datum BETWEEN date '2009-01-01' AND date '2009-12-31') S
GROUP BY month
HAVING sum(S.betrag) > (SELECT sum(betrag)/12 FROM Bestellungen WHERE datum BETWEEN date '2009-01-01' AND date '2009-12-31')
ORDER BY month;

--Aufgabe 11
(SELECT titel, film_id
FROM (Filme NATURAL JOIN Kategorien) NATURAL JOIN (Bestellposten NATURAL JOIN (Bestellungen NATURAL JOIN Kunden)) 
WHERE kategorie = 4 )
EXCEPT
(SELECT titel, film_id
FROM (Filme NATURAL JOIN Kategorien) NATURAL JOIN (Bestellposten NATURAL JOIN (Bestellungen NATURAL JOIN Kunden)) 
WHERE kategorie = 4 AND NOT geschlecht ='F')
ORDER BY film_id
;

--Aufgabe 12
SELECT count(DISTINCT kunden_id)
FROM (Filme NATURAL JOIN Kategorien) NATURAL JOIN (Bestellposten NATURAL JOIN (Bestellungen NATURAL JOIN Kunden))
WHERE kundenalter > 60 AND (oberkategorie IN (SELECT kategorie FROM Kategorien WHERE name = 'Horror') OR name = 'Horror');

--Aufgabe 13
SELECT count(*)
FROM (
SELECT DISTINCT kunden_id
FROM Filme NATURAL JOIN Bestellposten NATURAL JOIN Bestellungen
WHERE kategorie BETWEEN 1 AND 8
GROUP BY kunden_id
HAVING count(DISTINCT kategorie) = 8) S;

--B2 Wie viele Filme gibt es in der Kategorie Animation inklusive aller direkten und indirekten Unterkategorien.
WITH RECURSIVE AnimationFilms(k) AS (
VALUES(19)
UNION
(SELECT kategorie FROM Kategorien, AnimationFilms WHERE Kategorien.oberkategorie = AnimationFilms.k))

SELECT count(*) FROM Filme INNER JOIN AnimationFilms ON kategorie = k;
