package flinktableapi.UserDataTypes;

/**
 * Einfaches POJO zum modellieren von Teilen
 */

public class Teil{
    public long TNr;
    public String TName;
    public String Farbe;
    public long Gewicht;

    public Teil() {}

    public Teil(long TNr, String TName, String Farbe, long Gewicht) {
        this.TNr = TNr;
        this.TName = TName;
        this.Farbe = Farbe;
        this.Gewicht = Gewicht;
    }

    @Override
    public String toString() {
        return "Teil{" +
                "TNr=" + TNr +
                ", TName='" + TName + '\'' +
                ", Farbe='" + Farbe + '\'' +
                ", Gewicht=" + Gewicht +
                '}';
    }
}
