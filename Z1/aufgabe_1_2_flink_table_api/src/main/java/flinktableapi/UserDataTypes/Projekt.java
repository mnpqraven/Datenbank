package flinktableapi.UserDataTypes;

/**
 * Einfaches POJO zum modellieren von Projekten
 */

public class Projekt {
    public long PNr;
    public String PName;
    public String Ort;

    public Projekt() {}

    public Projekt(long PNr, String PName, String ort) {
        this.PNr = PNr;
        this.PName = PName;
        Ort = ort;
    }

    @Override
    public String toString() {
        return "Projekt{" +
                "PNr=" + PNr +
                ", PName='" + PName + '\'' +
                ", Ort='" + Ort + '\'' +
                '}';
    }
}
