/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion;

import servicio.objetos.Transaccion;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.xml.datatype.DatatypeConfigurationException;
import propio.Log;

/**
 *
 * @author rpalacic
 */
public class HiloComunicacionSerial implements Runnable {

    private static final int ALMACENAMIENTO = Control.getInstance().getAlamacenamiento();

    public HiloComunicacionSerial() {
    }

    private boolean conectado = true;
    private boolean correr = true;
    private int errores = 0;

    public void finalizar() {
        correr = false;
    }

    @Override
    public void run() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SerialPort puertoSerie = null;
        boolean ingreso = false;
        try {
            while (correr) {
                try {
                    //Recorrer lista de puertos seriales
                    Enumeration listaPuertos = CommPortIdentifier.getPortIdentifiers();
                    CommPortIdentifier idPuerto = null;
                    ingreso = true;
                    boolean encontrado = false;
                    while (listaPuertos.hasMoreElements() && !encontrado) {
                        idPuerto = (CommPortIdentifier) listaPuertos.nextElement();
                        if (idPuerto.getPortType() == CommPortIdentifier.PORT_SERIAL && idPuerto.getName().equals(Control.getInstance().getSerialPuerto())) {
                            encontrado = true;
                        }
                    }
                    if (!encontrado) {
                        throw new Exception("Puerto Serial no encontrado");
                    }
                    //Abrir puerto
                    //Aqui es donde se recerva y se habre el puerto serial
                    puertoSerie = (SerialPort) idPuerto.open("DescripcionPropietario", 2000);
                    //Configurar parámetros de comunicacion
                    puertoSerie.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    puertoSerie.setDTR(true);
                    puertoSerie.notifyOnDataAvailable(true);
                    //Escuchar puerto serial
                    InputStream entrada;
                    String s2 = "";
                    boolean _mensaje = false;
                    while (correr) {
                        try {
                            entrada = puertoSerie.getInputStream();
//                            intentos = 0;
                            while (entrada.available() > 0) {
                                _mensaje = true;
                                int _dec = entrada.read();
                                if (s2.equals("")) {
                                    s2 = String.valueOf(_dec);
                                } else {
                                    s2 = s2 + "-" + _dec;
                                }
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException ex) {
                                }
                            }
                            if (_mensaje && !s2.equals("")) {
                                Transaccion _trans = new Transaccion();
                                _trans.setValor(s2);
                                _trans.setSerial(Control.getInstance().getReceptorSerial());
                                _trans.setFechaHora(df.format(new Date()));
                                switch (ALMACENAMIENTO) {
                                    case Control.ALMACENAMIENTO_MEMORIA:
                                        Control.getInstance().addTransaccion(_trans);
                                        Control.getInstance().addTransaccionBrasystem(_trans); //Guardar en memoria transacciones Brasystem
                                        break;
                                    case Control.ALMACENAMIENTO_TEXTO:
                                        PendienteArchivo.getInstance().agregarTransaccion(_trans);
                                        break;
                                    default:
                                        throw new Exception("Forma de operacion no definida");
                                }
                                //Agregar al vercor en cola
                                System.out.println(s2.trim());
                                _mensaje = false;
                                s2 = "";
                            }
                        } catch (IOException | DatatypeConfigurationException ex) {
                            throw new Exception("Error en la lectura de una tramas (" + ex.getMessage() + ")");
                        }
                        errores = 0;
                        if (!conectado) {
                            conectado = true;
                            ComunicacionWS.errorSerial(Control.getInstance().getSerialPuerto(), true);
                        }
                    }
                } catch (PortInUseException ex) {
                    errores++;
                    Thread.sleep(20000);
                } catch (Exception ex) {
                    errores++;
                    Thread.sleep(20000);
                } finally {
                    if (!ingreso) {
                        throw new Exception("No fue posible conectarse con la operación serial");
//                        errores++;
//                        Thread.sleep(20000);
                    }
                    if (puertoSerie != null) {
                        puertoSerie.close();
                        puertoSerie = null;
                    }
                }
                if (errores >= 5) {
                    errores = 0;
                    Log.getInstance().error("ERROR PUERTO SERIAL", "Comunicación perdida con el receptos tras varios intentos, se intentara de nuevo en 1 minuto", new Exception());
                    if (conectado) {
                        conectado = false;
                        ComunicacionWS.errorSerial(Control.getInstance().getSerialPuerto(), false);
                    }
                    try {
                        Thread.sleep(60000);//Espera de 1 minuto
                    } catch (InterruptedException ex) {
                    }
                }
            }
        } catch (Exception ex) {
            Log.getInstance().error("ERROR FATAL puerto serial", "ERROR FATAL en el sistema... Operación finalizada", ex);
        } finally {
            Control.getInstance().desconectar();
            if (puertoSerie != null) {
                puertoSerie.close();
                puertoSerie = null;
            }
        }
    }

}
