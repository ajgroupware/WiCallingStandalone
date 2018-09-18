/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion;

/**
 *
 * @author SystemTech Integral
 */
import servicio.objetos.Transaccion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import propio.PropApl;

public class PendienteArchivo {

    private final String archivo;
    private final String archivoIntegracionBraSystem;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static PendienteArchivo instance = null;

    public PendienteArchivo() {
        String ruta = new File("").getAbsolutePath();
        String separador = (ruta.contains("\\")) ? "\\" : "/";
        String rutaLocal = PropApl.getInstance().get("ruta");
        if (rutaLocal.equals("")) {
            rutaLocal = ruta + separador;
        } else {
            rutaLocal += separador;
        }
        archivo = rutaLocal + "logs" + separador + "historial.txt";
        archivoIntegracionBraSystem = rutaLocal + "logs" + separador + "historial_brasystem.txt";
    }

    public static PendienteArchivo getInstance() {
        if (instance == null) {
            instance = new PendienteArchivo();
        }
        return instance;
    }

    public boolean agregarTransacciones(List<Transaccion> transacciones) {
        boolean exitos = false;
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo, true);
            
            pw = new PrintWriter(fichero);
            for (Transaccion transaccion : transacciones) {
                String _val = transaccion.getFechaHora()
                        + "&&" + String.valueOf(transaccion.getValor())
                        + "&&" + String.valueOf(transaccion.getSerial());
                 
                pw.println(_val);
            }
            exitos = true;
        } catch (IOException e) {
            exitos = false;
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }

            } catch (Exception e2) {

            }
        }
        return exitos;
    }
    
    public boolean agregarTransaccionesBrasystem(List<Transaccion> transacciones) {
        boolean exitos = false;
        FileWriter ficheroBraSystem = null;
        PrintWriter pwBraSystem = null;
        try {
            ficheroBraSystem = new FileWriter(archivoIntegracionBraSystem, true);
            
            pwBraSystem = new PrintWriter(ficheroBraSystem);
            for (Transaccion transaccion : transacciones) {
                
                String _valBrasystem = transaccion.getFechaHora().replaceAll(" ", "T")
                    + "&&" + String.valueOf(transaccion.getValor())
                    + "&&" + String.valueOf(transaccion.getSerial());   
                
                pwBraSystem.println(_valBrasystem);
            }
            exitos = true;
        } catch (IOException e) {
            exitos = false;
        } finally {
            try {
                if (null != ficheroBraSystem) {
                    ficheroBraSystem.close();
                }
            } catch (Exception e2) {

            }
        }
        return exitos;
    }

    public boolean agregarTransaccion(Transaccion transaccion) {
        boolean exitoso = false;
        FileWriter fichero = null;
        FileWriter ficheroBraSystem = null;
        PrintWriter pw = null;
        PrintWriter pwBraSystem = null;
        try {
            fichero = new FileWriter(archivo, true);
            ficheroBraSystem = new FileWriter(archivoIntegracionBraSystem, true);
            
            pw = new PrintWriter(fichero);
            pwBraSystem = new PrintWriter(ficheroBraSystem);
            String _val = transaccion.getFechaHora()
                    + "&&" + String.valueOf(transaccion.getValor())
                    + "&&" + String.valueOf(transaccion.getSerial());

            String _valBrasystem = transaccion.getFechaHora().replaceAll(" ", "T")
                    + "&&" + String.valueOf(transaccion.getValor())
                    + "&&" + String.valueOf(transaccion.getSerial());            
           
            pw.println(_val);
            pwBraSystem.println(_valBrasystem);
            exitoso = true;
        } catch (Exception e) {
            exitoso = false;
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
                
                if (null != ficheroBraSystem) {
                    ficheroBraSystem.close();
                }
            } catch (Exception e2) {
            }
        }
        return exitoso;
    }

    public List<Transaccion> retirarTransacciones() {
        List<Transaccion> transacciones = new ArrayList();
        File fichero = null;
        FileReader fr = null;
        BufferedReader br = null;
        boolean borrar = false;
        try {
            fichero = new File(archivo);
            if(fichero.exists()){
                fr = new FileReader(fichero);
                br = new BufferedReader(fr);
                String linea;
                Transaccion transaccion;
                while ((linea = br.readLine()) != null) {
                    transaccion = new Transaccion();
                    String[] ln = linea.trim().split("&&");
                    transaccion.setFechaHora(ln[0]);
                    transaccion.setValor(ln[1]);
                    transaccion.setSerial(ln[2]);
                    transacciones.add(transaccion);
                }
                borrar = true;
            }
        } catch (IOException | NumberFormatException e) {
            transacciones = new ArrayList();
            borrar = false;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
            }
        }
        if(borrar){
            fichero.delete();
        }else{
            transacciones = new ArrayList();
        }
        return transacciones;
    }
    
    
    public List<Transaccion> retirarTransaccionesBrasystem() {
        List<Transaccion> transacciones = new ArrayList();
        File fichero = null;
        FileReader fr = null;
        BufferedReader br = null;
        boolean borrar = false;
        try {
            fichero = new File(archivoIntegracionBraSystem);
            if(fichero.exists()){
                fr = new FileReader(fichero);
                br = new BufferedReader(fr);
                String linea;
                Transaccion transaccion;
                while ((linea = br.readLine()) != null) {
                    transaccion = new Transaccion();
                    String[] ln = linea.trim().split("&&");
                    transaccion.setFechaHora(ln[0]);
                    transaccion.setValor(ln[1]);
                    transaccion.setSerial(ln[2]);
                    transacciones.add(transaccion);
                }
                borrar = true;
            }
        } catch (IOException | NumberFormatException e) {
            transacciones = new ArrayList();
            borrar = false;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
            }
        }
        if(borrar){
            fichero.delete();
        }else{
            transacciones = new ArrayList();
        }
        return transacciones;
    }

}
