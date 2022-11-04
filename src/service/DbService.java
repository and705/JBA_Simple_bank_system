package service;

import model.Account;
import org.sqlite.SQLiteDataSource;

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

    public static boolean addIncome(String cardNumber, int income) {
        boolean result = false;
        String addIncome = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(addIncome)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, income);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
            conn.commit();
            result = true;
        } catch (SQLException throwables) {
            System.err.println("Error adding money");
            throwables.printStackTrace();
            result = false;
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;
    }

    public static boolean checkUser(String cardNumber) {
        ResultSet resultSet = null;
        boolean isCardExist = false;
        String query = "SELECT EXISTS (SELECT number FROM card WHERE number = ?);";
        //String query = "SELECT number FROM card GROUP BY id HAVING number = ?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cardNumber);
            resultSet = pstmt.executeQuery();
            isCardExist = resultSet.getBoolean(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isCardExist;
    }

    public static boolean transfer(String cardNumber, int sum) {
        boolean result = false;
        String update = "UPDATE card SET balance = ? WHERE number = ?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(update)) {
            conn.setAutoCommit(false);
            statement.setInt(1, sum);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
            conn.commit();
            result = true;
        } catch (SQLException throwables) {
            System.err.println("Error transferring money");
            throwables.printStackTrace();
            result = false;
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;
    }

    public static boolean subIncome(String cardNumber, int money) {
        boolean success = false;
        String update = "UPDATE card SET balance = balance - ? WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(update)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, money);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException throwables) {
            System.err.println("Error subtract money");
            throwables.printStackTrace();
            success = false;
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try (Connection conn = DriverManager.getConnection(url)) {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return success;
    }
    public static void closeAccount(String cardNumber) {
        String delete = "DELETE FROM card WHERE number = ?;";
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(delete)) {
            pstmt.setString(1, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeDbConnection() {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    }





