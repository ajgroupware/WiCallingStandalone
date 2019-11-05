/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import propio.PropApl;

/**
 *
 * @author alvarorodriguez
 */
public class SQLiteJDBCDriverConnection {
    
    private static Connection conn = null;
    /**
     * Connect to a sample database
     * @return Connection
     */
    public static Connection connect() {
        if (conn == null) {
            try {
                String ruta = new File("").getAbsolutePath();
                String separador = (ruta.contains("\\")) ? "\\" : "/";
                String rutaLocal = PropApl.getInstance().get("ruta");
                if (rutaLocal.equals("")) {
                    rutaLocal = ruta + separador;
                } else {
                    rutaLocal += separador;
                }
                // db parameters
                //String url = "jdbc:sqlite:/Users/alvarorodriguez/GroupWare/Projects/TIPI/workspace/servWiCloud/wicalling.db";
                String url = "jdbc:sqlite:" + rutaLocal + "wicalling2.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);
                System.out.println("--Connection to SQLite has been established.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                /*
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                */
            }
        } 
        
        return conn;
    }
}
