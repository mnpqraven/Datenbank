-- Anmerkung: Zu manchen Aufgaben werden mehrere Loesungsskizzen gegeben
-- Aufgabe 1 
SELECT bestell_id,
       datum,
       kunden_id,
       betrag
FROM   bestellungen
       NATURAL JOIN kunden
WHERE  username = 'user42';  

SELECT bestellungen.*
FROM   bestellungen
       NATURAL JOIN kunden
WHERE  username = 'user42';  
 

-- Aufgabe 2 
SELECT titel AS titel
FROM   filme
WHERE  film_id IN (SELECT film_id AS filmId
                   FROM   bestellposten
                   WHERE  bestell_id IN (SELECT bestell_id AS bestellId
                                         FROM   bestellungen
                                         WHERE  kunden_id = (SELECT
                                                kunden_id AS kundenId
                                                             FROM   kunden
                                                             WHERE
                                                username = 'user42'
                                                            )));  
            
SELECT titel
FROM   filme
       NATURAL JOIN bestellposten
       NATURAL JOIN bestellungen
       NATURAL JOIN kunden
WHERE  username = 'user42';  


 -- Aufgabe 3 
SELECT titel
FROM   bestellungen
       NATURAL JOIN bestellposten
       NATURAL JOIN filme
       NATURAL JOIN kategorien
WHERE  name = 'Sci-Fi'
       AND kunden_id = 7747;  
  

-- Aufgabe 4 
SELECT vorname,
       nachname
FROM   kunden
WHERE  NOT EXISTS (SELECT kunden_id
                   FROM   bestellungen
                   WHERE  bestellungen.kunden_id = kunden.kunden_id)
       AND land = 'Germany'; 

SELECT vorname,
       nachname
FROM   kunden
WHERE  land = 'Germany'
       AND kunden_id NOT IN (SELECT kunden_id
                             FROM   bestellungen);  

SELECT vorname,
       nachname
FROM   kunden
       LEFT OUTER JOIN bestellungen USING (kunden_id)
WHERE  bestell_id IS NULL
       AND land = 'Germany';

SELECT vorname,
       nachname
FROM   kunden
WHERE  land = 'Germany'
EXCEPT
SELECT vorname,
       nachname
FROM   kunden
       NATURAL JOIN bestellungen;  


-- Aufgabe 5
SELECT land
FROM   kunden
GROUP  BY land
ORDER  BY Count (land) ASC
LIMIT  1;  

SELECT land
FROM   kunden
GROUP  BY land
HAVING Count (land) <= ALL (SELECT Count (land)
                            FROM   kunden
                            GROUP  BY land);  


-- Aufgabe 6
SELECT Sum (betrag) AS umsatz
FROM   bestellungen
WHERE  datum BETWEEN DATE '2009-01-01' AND DATE '2009-01-31';  

SELECT Sum (betrag) AS umsatz
FROM   bestellungen
WHERE  datum >= DATE '2009-01-01'
       AND datum <= DATE '2009-01-31';  


-- Aufgabe 7   
SELECT Sum (-betrag * 0.25)
FROM   bestellungen
       NATURAL JOIN kunden
WHERE  kundenalter BETWEEN 16 AND 25
       AND datum BETWEEN DATE '2009-08-01' AND DATE '2009-08-31';  

SELECT Sum (CASE
              WHEN kundenalter BETWEEN 16 AND 25 THEN -betrag * 0.25
              ELSE 0
            END)
FROM   bestellungen
       NATURAL JOIN kunden
WHERE  datum BETWEEN DATE '2009-08-01' AND DATE '2009-08-31'; 

SELECT Sum (CASE
              WHEN kundenalter BETWEEN 16 AND 25
                   AND datum BETWEEN DATE '2009-08-01' AND DATE '2009-08-31'
            THEN
              -betrag * 0.25
              ELSE 0
            END)
FROM   bestellungen
       NATURAL JOIN kunden;  


-- Aufgabe 8  
SELECT k.name,
       Sum (views_anz)
FROM   kategorien k
       NATURAL JOIN filme
       NATURAL JOIN bestellposten
GROUP  BY k.name
ORDER  BY Sum (views_anz) DESC
LIMIT  1;  

WITH kategorie_gruppen
     AS (SELECT k.NAME,
                Sum (views_anz) AS views_anz
         FROM   kategorien k
                NATURAL JOIN filme
                NATURAL JOIN bestellposten
         GROUP  BY k.kategorie,
                   k.NAME)
SELECT *
FROM   kategorie_gruppen
WHERE  views_anz = (
                   -- >= ALL (SELECT sum_views_anz...
                   SELECT Max (views_anz)
                   FROM   kategorie_gruppen);  


-- Aufgabe 9
SELECT titel,
       Count (*)
FROM   bestellungen 
       NATURAL JOIN bestellposten 
       NATURAL JOIN filme
WHERE  datum BETWEEN DATE '2009-01-01' AND      DATE '2009-01-31'
GROUP  BY titel
ORDER  BY Count (*) DESC, titel
LIMIT  5; 


-- Aufgabe 10
SELECT Sum (betrag),
       Extract (month FROM datum)
FROM   bestellungen
WHERE  Extract (year FROM datum) = 2009
GROUP  BY Extract (month FROM datum)
HAVING Sum (betrag) > (SELECT Sum (betrag) / 12
                       FROM   bestellungen
                       WHERE  Extract (year FROM datum) = 2009)
ORDER  BY Extract (month FROM datum) ASC;  

WITH alle_bestellungen(monat, umsatz)
     AS (SELECT Extract (month FROM datum) AS monat,
                Sum (betrag)               AS umsatz
         FROM   bestellungen
         WHERE  Extract (year FROM datum) = 2009
         GROUP  BY Extract (month FROM datum)
         ORDER  BY monat ASC)
SELECT *
FROM   alle_bestellungen
WHERE  umsatz > (SELECT Avg (umsatz)
                 FROM   alle_bestellungen);  

SELECT monat_tab.durchschnitt, monat_tab.monat
FROM   (SELECT Extract (month FROM datum) AS monat,
               Sum (betrag)               AS durchschnitt
        FROM   bestellungen
        WHERE  datum BETWEEN DATE '2009-01-01' AND DATE '2009-12-31'
        GROUP  BY monat) monat_tab
WHERE  monat_tab.durchschnitt > (SELECT Sum (betrag) / 12
                                 FROM   bestellungen
								 WHERE  datum BETWEEN DATE '2009-01-01' AND DATE '2009-12-31')
ORDER  BY monat_tab.monat ASC;   


-- Aufgabe 11
WITH alle_bestellungen
     AS (SELECT *
         FROM   filme
                NATURAL JOIN bestellposten
                NATURAL JOIN bestellungen
                NATURAL JOIN kunden)
SELECT film_id,
       titel
FROM   alle_bestellungen
WHERE  film_id NOT IN (SELECT film_id
                       FROM   alle_bestellungen
                       WHERE  geschlecht LIKE 'M%')
       AND kategorie = 4
GROUP  BY film_id,
          titel
ORDER  BY film_id; 

SELECT DISTINCT film_id,
                titel
FROM   filme
       NATURAL JOIN bestellposten
       NATURAL JOIN bestellungen
       NATURAL JOIN kunden
WHERE  geschlecht = 'F'
       AND kategorie = 4
EXCEPT
SELECT DISTINCT film_id,
                titel
FROM   filme
       NATURAL JOIN bestellposten
       NATURAL JOIN bestellungen
       NATURAL JOIN kunden
WHERE  geschlecht = 'M'
       AND kategorie = 4
ORDER  BY film_id ASC;  

SELECT film_id,
       titel
FROM   filme
       NATURAL JOIN bestellposten
WHERE  film_id NOT IN (SELECT film_id
                       FROM   kategorien
                              NATURAL JOIN filme
                              NATURAL JOIN bestellposten
                              NATURAL JOIN bestellungen
                              NATURAL JOIN kunden
                       WHERE  geschlecht <> 'F'
                              AND kategorie = 4)
       AND kategorie = 4
GROUP  BY film_id,
          titel
ORDER  BY film_id ASC;  


-- Aufgabe 12
SELECT Count (DISTINCT kunden_id)
FROM   bestellposten
       NATURAL JOIN bestellungen
       NATURAL JOIN kunden
       NATURAL JOIN filme
       NATURAL JOIN kategorien k1
       JOIN kategorien k2
         ON ( k1.oberkategorie = k2.kategorie )
WHERE  k1.name = 'Horror'
        OR k2.name = 'Horror'
           AND kundenalter > 60;

-- rekursiv
WITH RECURSIVE horror_gesamt(kategorie_horror)
     AS (SELECT kategorie
         FROM   kategorien
         WHERE  name = 'Horror'
         UNION
         SELECT kategorie
         FROM   kategorien
         JOIN   horror_gesamt
         ON     oberkategorie = kategorie_horror)
SELECT Count ( DISTINCT kunden.kunden_id)
FROM   filme NATURAL
JOIN   bestellposten NATURAL
JOIN   bestellungen
JOIN   kunden
ON     kunden.kunden_id = bestellungen.kunden_id
JOIN   horror_gesamt
ON     kategorie_horror = kategorie
WHERE  kundenalter > 60;


-- Aufgabe 13
SELECT Count (kunden_id)
FROM   (SELECT kunden_id,
               Sum (DISTINCT d.kategorie)
        FROM   kunden k
               NATURAL JOIN bestellungen
               NATURAL JOIN bestellposten
               NATURAL JOIN filme d
        WHERE  d.kategorie BETWEEN 1 AND 8
        GROUP  BY kunden_id
        HAVING Sum (DISTINCT d.kategorie) = 36
       -- having count(distinct d.kategorie) = 8
       ) AS n;  

SELECT Count (*)
FROM   kunden
WHERE  NOT EXISTS (SELECT *
                   FROM   kategorien
                   WHERE  kategorie IN ( 1, 2, 3, 4,
                                         5, 6, 7, 8 )
                          AND NOT EXISTS (SELECT *
                                          FROM   bestellungen
                                                 JOIN bestellposten USING (
                                                 bestell_id)
                                                 JOIN filme USING (film_id)
                                          WHERE  kunden.kunden_id =
                                                 bestellungen.kunden_id
                                                 AND kategorien.kategorie =
                                                     filme.kategorie
                                         ));  


-- Bonusaufgabe B1
SELECT *
FROM   filme AS f1
WHERE  NOT EXISTS (SELECT tmp.*
                   FROM   (SELECT *
                           FROM   filme AS f2
                           WHERE  f1.film_id != f2.film_id) AS tmp
                   WHERE  ( ( f1.preis > tmp.preis
                               OR f1.spieldauer < tmp.spieldauer )
                             OR f1.preis >= tmp.preis
                                AND f1.spieldauer <= tmp.spieldauer )); 


-- Bonusaufgabe B2
WITH RECURSIVE animation(k)
     AS (SELECT kategorie
         FROM   kategorien
         WHERE  name = 'Animation'
         UNION
         SELECT kategorie
         FROM   kategorien
         JOIN   animation
         ON     (oberkategorie = k) )
SELECT Count (*)
FROM   animation
JOIN   kategorien
ON     (
              kategorie=k)
JOIN   filme
USING  (kategorie);                            


-- Bonusaufgabe B3
WITH daten
     AS (SELECT
                CASE
                       WHEN kundenalter < 30 THEN 'u30'
                       WHEN kundenalter < 60 THEN '30-59'
                       ELSE '60+'
                END AS altersgruppe,
                kunden.geschlecht,
                extract ( quarter FROM datum) AS quartal,
                betrag
         FROM   kunden NATURAL
         JOIN   bestellungen )
(
----- all
SELECT altersgruppe,
       quartal,
       geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY altersgruppe,
          quartal,
          geschlecht
UNION
----- one
SELECT altersgruppe,
       NULL AS quartal,
       NULL AS geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY altersgruppe
UNION
-----
SELECT NULL AS altersgruppe,
       quartal,
       NULL AS geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY quartal
UNION
-----
SELECT NULL AS altersgruppe,
       NULL AS quartal,
       geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY geschlecht
UNION
----- two
SELECT altersgruppe,
       quartal,
       NULL AS geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY altersgruppe,
       quartal
UNION
-----
SELECT altersgruppe,
       NULL AS quartal,
       geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY altersgruppe,
       geschlecht
UNION
-----
SELECT NULL AS altersgruppe,
       quartal,
       geschlecht,
       Sum (betrag)
FROM   daten
GROUP  BY quartal,
       geschlecht
UNION
----- none
SELECT NULL AS altersgruppe,
       NULL AS quartal,
       NULL AS geschlecht,
       Sum (betrag)
FROM   daten )
ORDER  BY altersgruppe ASC, quartal ASC, geschlecht ASC; 