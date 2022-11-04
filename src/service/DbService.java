package service;

import model.Account;

import java.sql.*;

public class DbService {

    String dbName;
    private static String fileSeparator = System.getProperty("file.separator");
    private static String url;

    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName; //fileSeparator + +  "Simple Banking System\task\\src\\db"
        DbService.url = url;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableCard() {
    // SQL statement for creating a new table
    String sql = "CREATE TABLE IF NOT EXISTS card (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	number text NOT NULL,\n"
            + "	pin text NOT NULL,\n"
            + "	balance integer\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(url);
    Statement stmt = conn.createStatement()) {
        // create a new table
        stmt.executeUpdate(sql);
            System.out.println("New table \"Card\" created");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

    public static void saveAccount(Account account) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, account.getCardNumber());
            s.setString(2, account.getPin());
            s.setInt(3, account.getBalance());

            s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Account getAccount(String cardNumber, String pin) {
        ResultSet resultSet = null;
        Account account = null;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn.isValid(0)) {
                String select = "SELECT number, pin, balance FROM card GROUP BY id HAVING number = ? AND pin = ?;";
                try (PreparedStatement pstmt = conn.prepareStatement(select)) {
                    pstmt.setString(1, cardNumber);
                    pstmt.setString(2, pin);
                    resultSet = pstmt.executeQuery();
                    if (!resultSet.next()) return account;
                    String number = resultSet.getString("number");
                    String PIN = resultSet.getString("pin");
                    int balance = resultSet.getInt("balance");
                    account = new Account(number, PIN, balance);
                } catch (SQLException ex) {
                    System.err.println("Error receiving data from database");
                    ex.printStackTrace();
                }
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return account;
    }


    }





