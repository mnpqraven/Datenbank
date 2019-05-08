package flinktableapi;

import flinktableapi.UserDataTypes.Lieferant;
import flinktableapi.UserDataTypes.Lieferung;
import flinktableapi.UserDataTypes.Projekt;
import flinktableapi.UserDataTypes.Teil;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;


/**
 * Vorlesung Datenbanksysteme
 * Uebungsblatt 2
 * Aufgabe 2.4: Apache Flink
 */
public class AufgabeTableAPI {

    /**
     * Die folgenden Tabellen entsprechen dem jeweiligen Schema aus Aufgabe 2.3
     */
    private static Table teile;
    private static Table lieferanten;
    private static Table projekte;
    private static Table lieferungen;

    private static ExecutionEnvironment env;
    private static BatchTableEnvironment tEnv;

    public static void main(String[] args) throws Exception {

        initialize();

        //////////
        // Beispiel: Welche Teile besitzen die Farbe Schwarz und wurden an ein Marburger Projekt geliefert
        //////////
        Table filtered = teile.filter("Farbe === 'Schwarz'")
                .join(lieferungen.as("LNr, lTNr, lPNr, Menge"))
                .where("TNr === lTNr")
                .join(projekte)
                .where("lPNr === PNr && Ort === 'Marburg'")
                .select("TNr, TName, Farbe, Gewicht");

        // Konvertieren des Tables 'filtered' in generisches DataSet.
        // Wichtig: Das Schema muss mit der gewählten Klasse übereinstimmen!
        DataSet<Teil> result = tEnv.toDataSet(filtered, Teil.class);

        // Ausgabe des Ergebnisses
        result.print();

        //////////
        // Hier Aufgabe 2.4 bearbeiten:
        //////////

        //////////
        // a)
        System.out.println("a) An wie viele Projekte wurden Teile geliefert, die mindestens 50kg gewogen haben?");
        Table a = teile.filter("Gewicht >= 50")
                .join(lieferungen.as("LNr, lTNr, PNr, Menge"))
                .where("TNr === lTNr")
                .select("PNr.count.distinct as cnt");
        DataSet<Long> resultA = tEnv.toDataSet(a, Long.class);
        resultA.print();
        //////////


        //////////
        // b)
        System.out.println("b) Die Namen der Lieferanten aufsteigend sortiert nach Gesamtliefermenge (Summe):");
        Table b = lieferanten.join(lieferungen.as("L, TNr, PNr, Menge"))
                .where("LNr === L")
                .groupBy("LName")
                .select("LName, Menge.sum as Summe")
                .orderBy("LName, Summe.asc");
        DataSet<String> resultB = tEnv.toDataSet(b.select("LName"), String.class);
        resultB.print();
        DataSet<Long> resultSumme = tEnv.toDataSet(b.select("Summe"), Long.class);
        resultSumme.print();
        //////////


        //////////
        // c)
        System.out.println("c) Woher kommen die Lieferanten, die weniger als 8 Lieferungen durchgeführt haben?");
        Table c = lieferungen.groupBy("LNr")
                .select("LNr, LNr.count as Anzahl")
                .join(lieferanten.as("L, LName, Ort"))
                .where("LNr === L")
                .filter("Anzahl < 8")
                .select("Ort");
        DataSet<String> resultC = tEnv.toDataSet(c, String.class);
        resultC.print();
        //////////


        //////////
        // d)
        System.out.println("d) Die Projektnummern der Projekte, die im Durchschnitt mehr als 60 schwarze Teile geliefert bekommen haben.");
        Table anzahlVonLieferungen = lieferungen.groupBy("PNr").select("PNr, LNr.count as AnzahlVonLieferungen");
        Table anzahlVonSchwarzenTeilen = teile.filter("Farbe === 'Schwarz'")
                .join(lieferungen.as("LNr, T, PNr, Menge"))
                .where("TNr === T")
                .groupBy("PNr")
                .select("PNr, Menge.sum as SchwarzeTeileSumme");
        Table d = anzahlVonLieferungen.join(anzahlVonSchwarzenTeilen.as("P, SchwarzeTeileSumme"))
                .where("PNr === P")
                .filter("(SchwarzeTeileSumme / AnzahlVonLieferungen) > 60")
                .select("PNr");
        DataSet<Long> resultD = tEnv.toDataSet(d, Long.class);
        resultD.print();
        //////////


    }


    /**
     * Initialisieren und laden einfacher Testdaten
     */
    public static void initialize() {

        env = ExecutionEnvironment.createCollectionsEnvironment();
        tEnv = BatchTableEnvironment.create(env);

        // Initialisiere Teile
        teile = tEnv.fromDataSet(
                env.fromElements(
                        new Teil(101, "Schraube A", "Grau", 3),
                        new Teil(102, "Schraube A", "Schwarz", 3),
                        new Teil(103, "Schraube B", "Grau", 4),
                        new Teil(104, "Schraube B", "Schwarz", 4),
                        new Teil(111, "Mutter A", "Grau", 1),
                        new Teil(112, "Mutter A", "Silber", 1),
                        new Teil(113, "Mutter A", "Schwarz", 1),
                        new Teil(114, "Mutter A", "Blau", 1),
                        new Teil(115, "Mutter B", "Grau", 2),
                        new Teil(116, "Mutter B", "Silber", 2),
                        new Teil(117, "Mutter B", "Schwarz", 2),
                        new Teil(118, "Mutter B", "Blau", 2),
                        new Teil(121, "Blech", "Grün", 50),
                        new Teil(122, "Blech", "Blau", 50),
                        new Teil(123, "Blech", "Gelb", 50),
                        new Teil(124, "Blech", "Rot", 50),
                        new Teil(131, "Rohr", "Schwarz", 30),
                        new Teil(132, "Rohr", "Grün", 30),
                        new Teil(133, "Rohr", "Rot", 30),
                        new Teil(134, "Rohr", "Blau", 30),
                        new Teil(141, "Brett", "Braun", 25),
                        new Teil(142, "Brett", "Braun", 50),
                        new Teil(143, "Brett", "Braun", 75)
                ), "TNr, TName, Farbe, Gewicht");

        // Initialisiere Lieferanten
        lieferanten = tEnv.fromDataSet(
                env.fromElements(
                        new Lieferant(1, "Schrauben Meier", "Marburg"),
                        new Lieferant(2, "Fachhandel Schmitt", "Gießen"),
                        new Lieferant(3, "Eisenwaren Schrotti", "Frankfurt"),
                        new Lieferant(4, "Gießerei AG", "Frankfurt"),
                        new Lieferant(5, "Baumarkt Gmbh", "Kassel")
                ), "LNr, LName, Ort");

        // Initialisiere Projekte
        projekte = tEnv.fromDataSet(
                env.fromElements(
                        new Projekt(1, "Weidenhäuser Brücke", "Marburg"),
                        new Projekt(2, "Flughafen BER", "Berlin"),
                        new Projekt(3, "Stuttgart 21", "Stuttgart"),
                        new Projekt(4, "Musterhaus", "Musterstadt"),
                        new Projekt(5, "Bustrasse Lahnberge", "Marburg")
                ), "PNr, PName, Ort");

        // Initialisiere Lieferungen
        lieferungen = tEnv.fromDataSet(
                env.fromElements(
                        new Lieferung(1, 102, 2, 100),
                        new Lieferung(1, 103, 1, 50),
                        new Lieferung(2, 114, 1, 20),
                        new Lieferung(5, 112, 3, 50),
                        new Lieferung(3, 102, 2, 70),
                        new Lieferung(3, 142, 1, 50),
                        new Lieferung(1, 123, 1, 35),
                        new Lieferung(5, 123, 3, 40),
                        new Lieferung(1, 102, 2, 90),
                        new Lieferung(1, 114, 1, 50),
                        new Lieferung(2, 114, 1, 100),
                        new Lieferung(2, 112, 3, 60),
                        new Lieferung(3, 132, 2, 10),
                        new Lieferung(5, 124, 1, 80),
                        new Lieferung(3, 102, 1, 68),
                        new Lieferung(3, 143, 3, 70),
                        new Lieferung(2, 112, 2, 50),
                        new Lieferung(1, 102, 1, 50),
                        new Lieferung(2, 134, 1, 20),
                        new Lieferung(3, 141, 3, 70),
                        new Lieferung(5, 115, 2, 10),
                        new Lieferung(3, 111, 1, 50),
                        new Lieferung(5, 103, 1, 80),
                        new Lieferung(1, 112, 3, 50),
                        new Lieferung(2, 131, 4, 100),
                        new Lieferung(1, 133, 1, 60),
                        new Lieferung(3, 143, 1, 60),
                        new Lieferung(3, 141, 3, 40),
                        new Lieferung(5, 102, 2, 35),
                        new Lieferung(1, 112, 1, 55),
                        new Lieferung(2, 114, 1, 40),
                        new Lieferung(2, 116, 3, 90)
                ), "LNr, TNr, PNr, Menge");

    }

}