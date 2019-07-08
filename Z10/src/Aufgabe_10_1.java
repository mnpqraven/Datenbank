import java.sql.*;
import java.util.Random;

public class Aufgabe_10_1 {
    private static final String url = "jdbc:postgresql://localhost/Datenbank"; // Ersetze Datenbank durch deinen Datenbankssnamen
    private static final String user = "postgres";
    private static final String password = ""; // Bitte hier dein Passwort für Postgresql eingeben.
    private static Connection con;
    private static Statement stmt;

    private static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void dropAllRandomTables() throws SQLException {
        stmt.executeUpdate("drop table if exists FirstRandomTable;");
        stmt.executeUpdate("drop table if exists SecondRandomTable;");
    }


    private static void createTwoRandomTables(int n) throws SQLException {
        dropAllRandomTables();
        System.out.println("Mit " + n + " Einträge:");
        stmt.executeUpdate("create table if not exists FirstRandomTable (i int not null, j int not null, diff int not null);");
        stmt.executeUpdate("create table if not exists SecondRandomTable (i int not null, j int not null, diff int not null);");
        long start, end;
        int batchSize = 1000, currentSize = batchSize;

        start = System.currentTimeMillis();
        for (int k = 0; k < n; k++) {
            int i = new Random().nextInt(100), j = new Random().nextInt(100);
            stmt.executeUpdate("insert into FirstRandomTable values (" + i + ", " + j + ", " + (i - j) + ");");
        }
        end = System.currentTimeMillis();
        System.out.println("Single update method time: " + (end - start) + " Millis.");

        start = System.currentTimeMillis();
        while (n > 0) {
            int i = new Random().nextInt(100), j = new Random().nextInt(100);
            stmt.addBatch("insert into SecondRandomTable values (" + i + ", " + j + ", " + (i - j) + ");");
            currentSize--;
            n--;
            if (currentSize == 0) {
                stmt.executeBatch();
                currentSize = batchSize;
            }
        }
        stmt.executeBatch();
        end = System.currentTimeMillis();
        System.out.println("Batch update method time: " + (end - start) + " Millis.");
        System.out.println();
    }

    public static void main(String[] args) throws SQLException {
        connect();
        createTwoRandomTables(500000);
    }
}
