<<<<<<< HEAD
package flinktableapi.UserDataTypes;

/**
 * Einfaches POJO zum modellieren von Lieferanten
 */

public class Lieferant{
    public long LNr;
    public String LName;
    public String Ort;

    public Lieferant() {}

    public Lieferant(long LNr, String LName, String Ort) {
        this.LNr = LNr;
        this.LName = LName;
        this.Ort = Ort;
    }

    @Override
    public String toString() {
        return "Lieferant{" +
                "lNnr=" + LNr +
                ", lName='" + LName + '\'' +
                ", lOrt='" + Ort + '\'' +
                '}';
    }
=======
package flinktableapi.UserDataTypes;

/**
 * Einfaches POJO zum modellieren von Lieferanten
 */

public class Lieferant{
    public long LNr;
    public String LName;
    public String Ort;

    public Lieferant() {}

    public Lieferant(long LNr, String LName, String Ort) {
        this.LNr = LNr;
        this.LName = LName;
        this.Ort = Ort;
    }

    @Override
    public String toString() {
        return "Lieferant{" +
                "lNnr=" + LNr +
                ", lName='" + LName + '\'' +
                ", lOrt='" + Ort + '\'' +
                '}';
    }
>>>>>>> master
}