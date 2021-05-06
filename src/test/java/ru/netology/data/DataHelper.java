package ru.netology.data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private static String url = "jdbc:mysql://localhost:3306/app";
    private static String user = "app";
    private static String password = "9mREsvXDs9Gk89Ef";

    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static void cleanData() throws SQLException {
        QueryRunner runner = new QueryRunner();
        val codes = "DELETE FROM auth_codes";
        val cards = "DELETE FROM cards";
        val users = "DELETE FROM users";

        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            runner.update(conn, codes);
            runner.update(conn, cards);
            runner.update(conn, users);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static String getUserId() {
        val getUserId = "SELECT id FROM users WHERE login = 'vasya';";
        try (
                val connect = DriverManager.getConnection(url, user, password);
                val createStmt = connect.createStatement();
        ) {
            try (val resultSet = createStmt.executeQuery(getUserId)) {
                if (resultSet.next()) {
                    val userId = resultSet.getString(1);
                    return userId;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static String getVerificationCode() {
        val requestCode = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1;";

        try (
                val connect = DriverManager.getConnection(url, user, password
                );
                val prepareStmt = connect.prepareStatement(requestCode);
        ) {
            prepareStmt.setString(1, getUserId());
            try (val resultSet = prepareStmt.executeQuery()) {
                if (resultSet.next()) {
                    val code = resultSet.getString(1);
                    return code;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static AuthInfo getInvalidLogin() {
        return new AuthInfo("notvalid", "qwerty123");
    }

    public static AuthInfo getInvalidPassword() {
        return new AuthInfo("vasya", "qqqqqqqq");
    }

    public static String invalidPassword() {
        return "aaaaaaa";
    }

    public static String getInvalidVerificationCode() {
        return "111111";
    }


}