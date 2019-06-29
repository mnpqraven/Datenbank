import java.sql.*;
import java.util.Scanner;

public class Aufgabe_9_1 {

    private static final String url = "jdbc:postgresql://localhost/Datenbank"; // Ersetze Datenbank durch deinen Datenbankssnamen
    private static final String user = "postgres";
    private static final String password = ""; // Bitte hier dein Passwort für Postgresql eingeben Dennis.

    public static void main(String[] args) {
        System.out.print("Type in a SQL Query: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(input);
            ResultSetMetaData rsm = rs.getMetaData();
            System.out.println("DATA OUTPUT");
            StringBuilder format = new StringBuilder();
            Object[] headers = new String[rsm.getColumnCount()];
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                format.append("|%").append(i).append("$-12s|");
                headers[i - 1] = rsm.getColumnName(i);
            }
            format.append("\n");
            System.out.format(format.toString(), headers);
            while (rs.next()) {
                Object[] tuple = new Object[rsm.getColumnCount()];
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    tuple[i - 1] = rs.getObject(i);
                }
                System.out.format(format.toString(), tuple);
            }
        } catch (Exception e) {
            System.out.println("SQL Problem");
        }
    }
}
