/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servicio.objetos.Transaccion;

/**
 *
 * @author alvarorodriguez
 */
public class TransaccionDAO {
    
    public static void addTransaccion(String valor, String serial, String fechaHora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = SQLiteJDBCDriverConnection.connect();
            String sql = "INSERT INTO Transaccion (valor, fechaHora, serial) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, valor );
            stmt.setString(2, fechaHora );
            stmt.setString(3, serial);
            
            int row = stmt.executeUpdate();
            if (row == 1) {
                System.out.println("--row " + valor + " " + fechaHora + " " + serial);
            } else {
                System.out.println("--row error insert");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    public static List<Transaccion> getTransaccion() {
        Connection dbConnection = null;
        Statement statement = null;
        String selectTableSQL = "SELECT id, valor, fechaHora, serial FROM Transaccion order by id limit 50;";
        List<Transaccion> transacciones = new ArrayList();
        try {
                dbConnection = SQLiteJDBCDriverConnection.connect();
                statement = dbConnection.createStatement();
                //System.out.println(selectTableSQL);
                // execute select SQL stetement
                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String valor = rs.getString("valor");
                    String fechaHora = rs.getString("fechaHora");
                    String serial = rs.getString("serial");

                    System.out.println("id : " + id);
                    System.out.println("valor : " + valor);
                    System.out.println("serial : " + serial);
                    System.out.println("fechaHora : " + fechaHora);  
                    System.out.println("---");
                    transacciones.add(new Transaccion(id, serial, valor, fechaHora.replaceAll("T", " ")));
                }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        
        return transacciones;
    }
    
    public static void deleteTransaccion(String listId) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String deleteSQL = "DELETE FROM Transaccion WHERE id in ("+ listId +")";

        try {
            dbConnection = SQLiteJDBCDriverConnection.connect();
            preparedStatement = dbConnection.prepareStatement(deleteSQL);
            // execute delete SQL stetement
            int rows = preparedStatement.executeUpdate();

            System.out.println("Record is deleted! " + rows);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
    
}
