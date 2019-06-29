import java.sql.*;

public class Aufgabe_9_3 {
    private static final String url = "jdbc:postgresql://localhost/Datenbank"; // Ersetze Datenbank durch deinen Datenbankssnamen
    private static final String user = "postgres";
    private static final String password = ""; // Bitte hier dein Passwort für Postgresql eingeben Dennis.
    private static Connection con;

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate("create or replace function youAreFired(pNummer IN INT) " +
                    "returns void as " +
                    "$$ begin " +
                    "IF pNummer IN (select pnr from Personal P " +
                    "where not exists (select mnr from PMzuteilung A1 where A1.pnr = P.pnr and not exists " +
                    "(select pnr from PMzuteilung A2 where not A2.pnr = P.pnr and A2.mnr = A1.mnr))) " +
                    "THEN " +
                    "delete from Personal P where P.pnr = pNummer; " +
                    "END IF; " +
                    "end; $$ " +
                    "LANGUAGE PLPGSQL;");

            con.commit();
            con.close();
        } catch (Exception e) {
            con.rollback();
            System.out.println("SQL Problem - Rolled back");
        }
    }
}
