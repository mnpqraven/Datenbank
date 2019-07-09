import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class Aufgabe_9_2 {
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

    // a
    public static void createTablePoints() throws SQLException {
        stmt.executeUpdate("create table if not exists points (point_id int primary key, point_x int not null, point_y int not null);");
    }
    public static void dropTablePoints() throws SQLException {
        stmt.executeUpdate("drop table if exists points;");
    }

    // b
    public static void loadTuples(String inputPath) throws FileNotFoundException, SQLException {
        Scanner scanner = new Scanner(new File(inputPath));
        while (scanner.hasNext()) {
            stmt.executeUpdate("insert into points values (" + scanner.nextLine() + ");");
        }
    }

    // c
    public static void loadTuplesPrepared(String inputPath) throws SQLException, FileNotFoundException {
        PreparedStatement pstmt = con.prepareStatement("insert into points values (?, ?, ?);");
        Scanner scanner = new Scanner(new File(inputPath));
        while (scanner.hasNext()) {
            String[] parameters = scanner.nextLine().split(",");
            pstmt.setInt(1, Integer.valueOf(parameters[0]));
            pstmt.setInt(2, Integer.valueOf(parameters[1]));
            pstmt.setInt(3, Integer.valueOf(parameters[2]));
            pstmt.execute();
        }
    }

    // d
    public static void loadTuplesBatch(String inputPath, int batchSize) throws FileNotFoundException, SQLException {
        PreparedStatement pstmt = con.prepareStatement("insert into points values (?, ?, ?);");
        Scanner scanner = new Scanner(new File(inputPath));
        int currentSize = batchSize;
        while (scanner.hasNext()) {
            String[] parameters = scanner.nextLine().split(",");
            pstmt.setInt(1, Integer.valueOf(parameters[0]));
            pstmt.setInt(2, Integer.valueOf(parameters[1]));
            pstmt.setInt(3, Integer.valueOf(parameters[2]));
            pstmt.addBatch();
            currentSize--;
            if (currentSize == 0) {
                pstmt.executeBatch();
                currentSize = batchSize;
            }
        }
        pstmt.executeBatch();
    }

    // e
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        connect();
        long start, end, diff;

        dropTablePoints();
        createTablePoints();
        start = System.currentTimeMillis();
        loadTuples("points.csv");
        end = System.currentTimeMillis();
        diff = end - start;
        System.out.println("Laufzeit von loadTuples: " + diff + "millisekunden.");
        System.out.println("Geschwindigkeitsänderung von loadTuples: Je Ladeoperation " + ((double) diff / 250000) + " millisekunden.");
        System.out.println("\nlangsamer als\n");

        dropTablePoints();
        createTablePoints();
        start = System.currentTimeMillis();
        loadTuplesPrepared("points.csv");
        end = System.currentTimeMillis();
        diff = end - start;
        System.out.println("Laufzeit von loadTuplesPrepared: " + diff + "millisekunden.");
        System.out.println("Geschwindigkeitsänderung von loadTuplesPrepared: Je Ladeoperation " + ((double) diff / 250000) + " millisekunden.");
        System.out.println("\nlangsamer als\n");

        dropTablePoints();
        createTablePoints();
        start = System.currentTimeMillis();
        loadTuplesBatch("points.csv", 10);
        end = System.currentTimeMillis();
        diff = end - start;
        System.out.println("Laufzeit von loadTuplesBatch mit Batchgröße 10: " + diff + "millisekunden.");
        System.out.println("Geschwindigkeitsänderung von loadTuplesBatch mit Batchgröße 10: Je Ladeoperation " + ((double) diff / 250000) + " millisekunden.");
        System.out.println("\nlangsamer als\n");

        dropTablePoints();
        createTablePoints();
        start = System.currentTimeMillis();
        loadTuplesBatch("points.csv", 100);
        end = System.currentTimeMillis();
        diff = end - start;
        System.out.println("Laufzeit von loadTuplesBatch mit Batchgröße 100: " + diff + "millisekunden.");
        System.out.println("Geschwindigkeitsänderung von loadTuplesBatch mit Batchgröße 100: Je Ladeoperation " + ((double) diff / 250000) + " millisekunden.");
        System.out.println("\nlangsamer als\n");

        dropTablePoints();
        createTablePoints();
        start = System.currentTimeMillis();
        loadTuplesBatch("points.csv", 1000);
        end = System.currentTimeMillis();
        diff = end - start;
        System.out.println("Laufzeit von loadTuplesBatch mit Batchgröße 1000: " + diff + "millisekunden.");
        System.out.println("Geschwindigkeitsänderung von loadTuplesBatch Batchgröße 1000: Je Ladeoperation " + ((double) diff / 250000) + " millisekunden.");
    }
}
