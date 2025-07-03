/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EasyBuy;

/**
 *
 * @author Umar
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=EasyBuy;encrypt=true;trustServerCertificate=true";
        String user = "Projek";
        String pass = "basisdata"; // ganti jika MySQL Anda pakai password

        return DriverManager.getConnection(url, user, pass);
    }
}
