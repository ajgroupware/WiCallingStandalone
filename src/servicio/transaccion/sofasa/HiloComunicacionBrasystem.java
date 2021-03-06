/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion.sofasa;

import servicio.objetos.Transaccion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import servicio.TransaccionDAO;
import servicio.transaccion.Control;
import servicio.transaccion.PendienteArchivo;

/**
 *
 * @author rpalacic
 */
public class HiloComunicacionBrasystem implements Runnable {

    private static final int ALMACENAMIENTO = Control.getInstance().getAlamacenamiento();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HiloComunicacionBrasystem() {
    }

    private boolean correr = true;

    public void finalizar() {
        correr = false;
    }

    @Override
    public void run() {
        try {
            int intentos = 0;
            int validar = 0;
            while (correr) {
                try {
                    List<Transaccion> transacciones = new ArrayList();
                    
                    switch (ALMACENAMIENTO) {
                        case Control.ALMACENAMIENTO_MEMORIA:
                            transacciones = Control.getInstance().getTransaccionesBrasystem();
                            break;
                        case Control.ALMACENAMIENTO_TEXTO:
                            //transacciones = PendienteArchivo.getInstance().retirarTransaccionesBrasystem();
                            transacciones = TransaccionDAO.getTransaccion();
                            break;
                        default:
                            Log.getInstance().error("ERROR DESCONOCIDO", "Forma de operacion no definida", new Exception("Forma de operacion no definida"));
                            throw new Exception("Forma de operacion no definida");
                    }
                    
                    if (transacciones.size() > 0) {//Hay Transaciones
                        boolean envio = false;
                        switch (ALMACENAMIENTO) {
                            case Control.ALMACENAMIENTO_MEMORIA:
                                envio = enviarTramasMemoria(transacciones);
                                break;
                            case Control.ALMACENAMIENTO_TEXTO:
                                envio = enviarTramasTexto(transacciones);
                                break;
                            default:
                                Log.getInstance().error("ERROR DESCONOCIDO", "Forma de operacion no definida", new Exception("Forma de operacion no definida"));
                                throw new Exception("Forma de operacion no definida");
                        }
                        
                        if (envio) {
                            validar = 0;
                            intentos = 0;
                        } else if (validar == 60) {
                            validar = 0;
                            intentos++;
                        } else {
                            validar++;
                        }
                        
                    } else { 
                        //No hay transacciones
                        if (validar == 60) {//Cada 60 iteraciones (1 minuto aprox)
                            try {
                                System.out.println("--No hay transacciones");
                                //ComunicacionWS.verificar(Control.getInstance().getSerialPuerto());
                            } catch (Exception e) {
                                intentos++;
                            }
                            validar = 0;
                        } else {
                            validar++;
                        }
                    }
                    if (intentos >= 5) {//Despues de 5 intentos
                        intentos = 0;
                        validar = 0;
                        throw new Exception("Comunicación con el servidor perdida tras varios intentos");
                    }
                } catch (Exception ex) {
                    Log.getInstance().error("ERROR COMUNICACION PERDIDA", "No se ha logrado entablar comunicación con el servidor, se intentará nuevamenmte en 1 minuto", ex);
                } finally {
                    try {
                        Thread.sleep(5000);//Espera 5 segundos
                    } catch (InterruptedException e) {
                        
                    }
                }
            }
        } catch (Exception ex) {
            Log.getInstance().error("ERROR FATAL comunicación Servidor", "ERROR FATAL en el sistema... Operación finalizada", ex);
        } finally {
            //Control.getInstance().desconectar();
        }
    }

    private boolean enviarTramasMemoria(List<Transaccion> transacciones) {
        boolean envio = false;
        try {
            if (transacciones != null) {
                for (Transaccion transacionEnv : transacciones) {
                    if (ComunicacionWS.enviarTransaccionesBrasystem(transacionEnv)) {
                        envio = true;
                        for (Transaccion transaccion : Control.getInstance().getTransaccionesBrasystem()) {
                            if (transacionEnv.equals(transaccion)) {
                                Control.getInstance().getTransaccionesBrasystem().remove(transaccion);
                                df.format(transaccion.getFechaHora());
                                Log.getInstance().suceso("ENVIO LLAMADO", "Fecha: " + transaccion.getFechaHora() + " - Pulsador: " + transaccion.getValor());
                                break;
                            }
                        }
                    }
                }
            }
            /*
            if (ComunicacionWS.enviarTransaccionesBrasystem(transacciones)) {
                envio = true;
                for (Transaccion transacionEnv : transacciones) {
                    for (Transaccion transaccion : Control.getInstance().getTransacciones()) {
                        if (transacionEnv.equals(transaccion)) {
                            Control.getInstance().getTransacciones().remove(transaccion);
                            df.format(transaccion.getFechaHora());
                            Log.getInstance().suceso("ENVIO LLAMADO", "Fecha: " + transaccion.getFechaHora() + " - Pulsador:" + transaccion.getValor());
                            break;
                        }
                    }
                }
            }
            */
        } catch (Exception ex) {
            envio = false;
        }
        return envio;
    }

    private boolean enviarTramasTexto(List<Transaccion> transacciones) {
        boolean envio = false;
        try {
            if (transacciones != null) {
                StringBuilder listIdTransaccion = new StringBuilder();
                List<Transaccion> listErrorTrans = new ArrayList<>();
                List<String> listError = new ArrayList<>();
                for (Transaccion transaccion : transacciones) {
                    if (ComunicacionWS.enviarTransaccionesBrasystem(transaccion)) {
                        envio = true;
                        Log.getInstance().suceso("ENVIO LLAMADO", "Fecha: " + transaccion.getFechaHora() + " - Pulsador: " + transaccion.getValor());
                        listIdTransaccion.append(transaccion.getId() + ",");
                    } else {
                        Log.getInstance().error("Error ENVIO LLAMADO", "Fecha: " + transaccion.getFechaHora() + " - Pulsador: " + transaccion.getValor(), new Exception("Error ENVIO LLAMADO"));
                        String error = "Error ENVIO LLAMADO Fecha: " + df.format(new Date()) + " - Pulsador: " + transaccion.getSerial() + " " + transaccion.getValor();
                        listError.add(error);
                        listErrorTrans.add(transaccion);
                    }
                }
                
                if (listErrorTrans.size() > 0) {
                    //PendienteArchivo.getInstance().agregarTransaccionesBrasystem(transacciones);
                    StringBuilder error = new StringBuilder();
                    for (String errorStr: listError) {
                        error.append(errorStr + "\n");
                    }
                    //Enviar email
                    //SendEmailUtil.sendTextEmail("WiCalling Error", error.toString(), Control.getInstance().getWsEventsMail());
                } 
                
                //Borrar transacciones enviadas
                if (listIdTransaccion.length() > 1) {
                    listIdTransaccion.deleteCharAt(listIdTransaccion.length() - 1);
                    TransaccionDAO.deleteTransaccion(listIdTransaccion.toString());

                }
            }
        } catch (Exception ex) {
            envio = false;
            //PendienteArchivo.getInstance().agregarTransaccionesBrasystem(transacciones);
        }
        return envio;
    }

}
