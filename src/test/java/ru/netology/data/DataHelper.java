package ru.netology.data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    String url = "jdbc:mysql://localhost:3306/app";
    String user = "app";
    String password = "9mREsvXDs9Gk89Ef";

    public DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public void cleanData() throws SQLException {
        QueryRunner runner = new QueryRunner();
        val codes = "DELETE FROM auth_codes";

        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            runner.update(conn, codes);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public class VerificationCode {
        private String code;
    }

    public String getUserId() {
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

    public String getVerificationCode() {
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

    public AuthInfo getInvalidLogin() {
        return new AuthInfo("notvalid", "qwerty123");
    }

    public AuthInfo getInvalidPassword() {
        return new AuthInfo("vasya", "qqqqqqqq");
    }

    public String invalidPassword() {
        return "aaaaaaa";
    }

    public String getInvalidVerificationCode() {
        return "111111";
    }


}