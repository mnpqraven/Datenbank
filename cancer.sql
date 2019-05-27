-- Eis(id, sorte, preis)
-- Verkauf (id, datum, eis_id, menge)

--a
SELECT sorte, preis
FROM Eis
ORDER BY preis LIMIT 1;
--
SELECT sorte, preis
FROM Eis
WHERE preis >= all(SELECT preis FROM Eis);

--b
SELECT sorte
FROM Eis LEFT OUTER JOIN (SELECT eis_id FROM Verkauf) ON Eis.id = Verkauf.eis_id
WHERE Eis.id in NULL;
--
SELECT sorte
FROM Eis e
WHERE NOT EXISTS(
    SELECT * FROM Verkauf v WHERE e.id = v.eis_id
);


--c
SELECT datum
FROM Verkauf NATURAL JOIN Eis
WHERE count(DISTINCT sorte >= ALL(SELECT sorte FROM Eis))
GROUP BY datum;
