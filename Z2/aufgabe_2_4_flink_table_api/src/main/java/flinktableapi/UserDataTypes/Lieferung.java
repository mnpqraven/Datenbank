package flinktableapi.UserDataTypes;

/**
 * Einfaches POJO zum modellieren von Lieferungen
 */

public class Lieferung{
    public long LNr;
    public long TNr;
    public long PNr;
    public long Menge;

    public Lieferung() {}

    public Lieferung(long LNr, long TNr, long PNr, long Menge) {
        this.LNr = LNr;
        this.TNr = TNr;
        this.PNr = PNr;
        this.Menge = Menge;
    }

    @Override
    public String toString() {
        return "Lieferung{" +
                "LNr=" + LNr +
                ", TNr=" + TNr +
                ", PNr=" + PNr +
                ", Menge=" + Menge +
                '}';
    }
}
