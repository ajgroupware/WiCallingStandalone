/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion;

import java.io.File;
import java.util.Calendar;
import propio.PropApl;

/**
 *
 * @author rpalacic
 */
public class HiloLimpiarLogs implements Runnable {

    public HiloLimpiarLogs() {
        rutaLocal = new File("").getAbsolutePath();
        separador = separador = (rutaLocal.contains("\\"))?"\\":"/";
    }

    private boolean correr = true;
    private final String rutaLocal;
    private String separador;

    public void finalizar() {
        correr = false;
    }

    @Override
    public void run() {
        String rutaLogs = PropApl.getInstance().get("ruta");
        if(rutaLogs.equals("")){
            rutaLogs = rutaLocal + separador + "logs" + separador;
        }else{
            rutaLogs += separador + "logs" + separador;
        }
        while (correr) {
            try {
                File directorio = new File(rutaLogs);
                if (directorio.exists()) {
                    File[] ficheros = directorio.listFiles();
                    for (File fichero : ficheros) {
                        try {
                            String fecha[] = (fichero.getName().trim().split("_"))[0].trim().split("-");
                            Calendar fhArchivo = Calendar.getInstance();
                            fhArchivo.set(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[2]), 0, 0, 0);
                            fhArchivo.set(Calendar.MILLISECOND, 0);
                            fhArchivo.add(Calendar.MONTH, 1);
                            Calendar fhActual = Calendar.getInstance();
                            fhActual.set(Calendar.HOUR, 0);
                            fhActual.set(Calendar.MINUTE, 0);
                            fhActual.set(Calendar.SECOND, 0);
                            fhActual.set(Calendar.MILLISECOND, 0);
                            if (fhArchivo.before(fhActual)) {
                                fichero.delete();
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            } catch (Exception ex) {

            } finally {
                try {
                    Thread.sleep(86400000);//Espera 1 día
                } catch (InterruptedException e) {
                }
            }
        }
    }

}
