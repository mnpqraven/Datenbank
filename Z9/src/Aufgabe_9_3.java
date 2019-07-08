import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Aufgabe_9_3 {
    private static final String url = "jdbc:postgresql://localhost/Datenbank"; // Ersetze Datenbank durch deinen Datenbankssnamen
    private static final String user = "postgres";
    private static final String password = ""; // Bitte hier dein Passwort für Postgresql eingeben.

    private static void youAreFired(int pnr) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        try {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            ResultSet mnrOperatedByPnr = stmt.executeQuery("select mnr from pmzuteilung where pnr = " + pnr + ";");
            List<Integer> mnrOfPnr = new ArrayList<>();
            while (mnrOperatedByPnr.next()) {
                mnrOfPnr.add(mnrOperatedByPnr.getInt(1));
            }
            stmt.executeUpdate("delete from personal where pnr = " + pnr + ";");
            for (Integer mnr : mnrOfPnr) {
                ResultSet otherThatCanOperateMnrOfPnr = stmt.executeQuery("select pnr from pmzuteilung where mnr = " + mnr + ";");
                if (!otherThatCanOperateMnrOfPnr.next()) {
                  System.out.println(pnr + " can not be fired!");
                  con.rollback();
                  return;
                }
            }
            con.commit();
            con.close();
            System.out.println(pnr + " (fired) deleted from personal.");
        } catch (Exception e) {
            con.rollback();
            System.out.println("SQL Problem - Roll back!");
        }
    }

    public static void main(String[] args) throws SQLException {
        // 67, 114 and 51 can operate Machine 93
        youAreFired(67); // Can be deleted
        youAreFired(114); // Can be deleted
        youAreFired(51); // Can NOT be deleted!
    }
}
