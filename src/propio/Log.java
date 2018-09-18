/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package propio;

/**
 *
 * @author SystemTech Integral
 */
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    private static Log instance = null;

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void error(String nombre, String mensaje, Exception e) {
        Logger logger = Logger.getLogger(nombre);
        FileHandler fh;
        try {
            String ruta = new File("").getAbsolutePath();
            String separador = (ruta.contains("\\")) ? "\\" : "/";
            String rutaLocal = PropApl.getInstance().get("ruta");
            if (rutaLocal.equals("")) {
                rutaLocal = ruta + separador;
            } else {
                rutaLocal += separador;
            }
            fh = new FileHandler(rutaLocal + "logs" + separador + formato() + "_error.txt", true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.log(Level.WARNING, "(" + nombre + ") " + mensaje, e);
            fh.close();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }

    public void suceso(String nombre, String suceso) {
        Logger logger = Logger.getLogger(nombre);
        FileHandler fh;
        try {
            String ruta = new File("").getAbsolutePath();
            String separador = (ruta.contains("\\")) ? "\\" : "/";
            String rutaLocal = PropApl.getInstance().get("ruta");
            if (rutaLocal.equals("")) {
                rutaLocal = ruta + separador;
            } else {
                rutaLocal += separador;
            }
            fh = new FileHandler(rutaLocal + "logs" + separador + formato() + "_suceso.txt", true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.log(Level.INFO, "{0} {1}", new Object[]{nombre, suceso});
            fh.close();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }

    private String formato() {
        GregorianCalendar fh = new GregorianCalendar();
        String str = String.valueOf(fh.get(GregorianCalendar.YEAR)) + "-"
                + String.valueOf(fh.get(GregorianCalendar.MONTH) + 1) + "-"
                + String.valueOf(fh.get(GregorianCalendar.DAY_OF_MONTH));
        return str;
    }
}
