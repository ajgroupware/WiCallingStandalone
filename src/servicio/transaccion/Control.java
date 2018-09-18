/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion;

import servicio.transaccion.sofasa.HiloComunicacionBrasystem;
import servicio.objetos.Transaccion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import propio.Log;
import propio.PropApl;
import servicio.transaccion.sofasa.SendEmailUtil;

/**
 *
 * @author rpalacic
 */
public class Control {

    private static Control instance = null;

    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }
        return instance;
    }

    public static final int ESTADO_DESCONECTADO = 0;
    public static final int ESTADO_CONECTADO = 1;
    public static final int ESTADO_CONFIGURACION = 2;

    public static final int ALMACENAMIENTO_MEMORIA = 0;
    public static final int ALMACENAMIENTO_TEXTO = 1;

    private Thread hiloComunicacionWs = null;
    private HiloComunicacionWS objetoComunicacionWs = null;
    private Thread hiloComunicacionBrasystem = null;
    private HiloComunicacionBrasystem objetoComunicacionBrasystem = null;
    private Thread hiloComunicacionSerial = null;
    private HiloComunicacionSerial objetoComunicacionSerial = null;
    private Thread hiloLimpiarLogs = null;
    private HiloLimpiarLogs objetoLimpiarLogs = null;

    private boolean enviandoTramas = false;
    private int estado = ESTADO_CONFIGURACION;

    private String wsRuta;
    private String wsUsuario;
    private String wsContrasena;
    private String serialPuerto;
    private String receptorSerial;
    private Integer alamacenamiento;
    private List<Transaccion> transacciones = new ArrayList();
    //Datos de configuración integración Sofasa
    private List<Transaccion> transaccionesBrasystem = new ArrayList();
    private String wsEndpoint;
    private String wsAuthenticationKey;
    private String wsEventsMail;

    public boolean isEnviandoTramas() {
        return enviandoTramas;
    }

    public void setEnviandoTramas(boolean enviandoTramas) {
        this.enviandoTramas = enviandoTramas;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getWsRuta() {
        return wsRuta;
    }

    public void setWsRuta(String wsRuta) {
        this.wsRuta = wsRuta;
    }

    public String getWsUsuario() {
        return wsUsuario;
    }

    public void setWsUsuario(String wsUsuario) {
        this.wsUsuario = wsUsuario;
    }

    public String getWsContrasena() {
        return wsContrasena;
    }

    public void setWsContrasena(String wsContrasena) {
        this.wsContrasena = wsContrasena;
    }

    public String getSerialPuerto() {
        return serialPuerto;
    }

    public void setSerialPuerto(String serialPuerto) {
        this.serialPuerto = serialPuerto;
    }

    public String getReceptorSerial() {
        return receptorSerial;
    }

    public void setReceptorSerial(String receptorSerial) {
        this.receptorSerial = receptorSerial;
    }

    public Integer getAlamacenamiento() {
        return alamacenamiento;
    }

    public void setAlamacenamiento(Integer alamacenamiento) {
        this.alamacenamiento = alamacenamiento;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public List<Transaccion> getTransaccionesBrasystem() {
        return transaccionesBrasystem;
    }

    public void setTransaccionesBrasystem(List<Transaccion> transaccionesBrasystem) {
        this.transaccionesBrasystem = transaccionesBrasystem;
    }

    public String getWsEndpoint() {
        return wsEndpoint;
    }

    public void setWsEndpoint(String wsEndpoint) {
        this.wsEndpoint = wsEndpoint;
    }

    public String getWsAuthenticationKey() {
        return wsAuthenticationKey;
    }

    public void setWsAuthenticationKey(String wsAuthenticationKey) {
        this.wsAuthenticationKey = wsAuthenticationKey;
    }

    public String getWsEventsMail() {
        return wsEventsMail;
    }

    public void setWsEventsMail(String wsEventsMail) {
        this.wsEventsMail = wsEventsMail;
    }
    
    public void addTransaccion(Transaccion _transaccion) {
        this.transacciones.add(_transaccion);
    }

    public void delTransaccion(int _pos) {
        if (this.transacciones.size() > _pos) {
            this.transacciones.remove(_pos);
        }
    }

    public Transaccion getTransaccion(int _pos) {
        if (this.transacciones.size() > _pos) {
            return getTransaccion(_pos);
        } else {
            return null;
        }
    }
    
    public void addTransaccionBrasystem(Transaccion _transaccion) {
        this.transaccionesBrasystem.add(_transaccion);
    }

    public void delTransaccionBrasystem(int _pos) {
        if (this.transaccionesBrasystem.size() > _pos) {
            this.transaccionesBrasystem.remove(_pos);
        }
    }

    public Transaccion getTransaccionBrasystem(int _pos) {
        if (this.transaccionesBrasystem.size() > _pos) {
            return getTransaccion(_pos);
        } else {
            return null;
        }
    }

    public void hiloComunicacionWsIniciar() {
        objetoComunicacionWs = new HiloComunicacionWS();
        hiloComunicacionWs = new Thread(objetoComunicacionWs);
        hiloComunicacionWs.start();
    }
    
    public void hiloComunicacionBrasystemIniciar() {
        objetoComunicacionBrasystem = new HiloComunicacionBrasystem();
        hiloComunicacionBrasystem = new Thread(objetoComunicacionBrasystem);
        hiloComunicacionBrasystem.start();
    }

    public void hiloComunicacionWsFinalizar() {
        if (objetoComunicacionWs != null) {
            objetoComunicacionWs.finalizar();
        }
    }
    
    public void hiloComunicacionBrasystemFinalizar() {
        if (objetoComunicacionBrasystem != null) {
            objetoComunicacionBrasystem.finalizar();
        }
    }

    public void hiloComunicacionSerialIniciar() {
        objetoComunicacionSerial = new HiloComunicacionSerial();
        hiloComunicacionSerial = new Thread(objetoComunicacionSerial);
        hiloComunicacionSerial.start();
    }

    public void hiloComunicacionSerialFinalizar() {
        if (objetoComunicacionSerial != null) {
            objetoComunicacionSerial.finalizar();
        }
    }

    public void hiloLimpiarLogsIniciar() {
        objetoLimpiarLogs = new HiloLimpiarLogs();
        hiloLimpiarLogs = new Thread(objetoLimpiarLogs);
        hiloLimpiarLogs.start();
    }

    public void hiloLimpiarLogsFinalizar() {
        if (objetoLimpiarLogs != null) {
            objetoLimpiarLogs.finalizar();
        }
    }

    public void desconectar() {
        hiloComunicacionSerialFinalizar();
        hiloComunicacionWsFinalizar();
        hiloLimpiarLogsFinalizar();
        hiloComunicacionBrasystemFinalizar();
        setEstado(ESTADO_DESCONECTADO);
        Log.getInstance().suceso("Fin del servicio", "Por algún motivo desconocido, el servicio finalizo su operación");
        servicio.transaccion.sofasa.Log.getInstance().suceso("Fin del servicio", "Por algún motivo desconocido, el servicio finalizo su operación");
    }

    public boolean cargarConfiguracion() throws Exception {
        boolean carga = false;
        String ruta = new File("").getAbsolutePath();
        String separador = (ruta.contains("\\")) ? "\\" : "/";
        String rutaLocal = PropApl.getInstance().get("ruta");
        if (rutaLocal.equals("")) {
            rutaLocal = ruta + separador;
        } else {
            rutaLocal += separador;
        }
        File f = new File(rutaLocal + "config.txt");
        BufferedReader entrada;
        try {
            entrada = new BufferedReader(new FileReader(f));
            //Variables de conexión WS
            if (entrada.ready()) {
                String[] variables = entrada.readLine().trim().split("&");
                if (variables.length == 3) {
                    Control.getInstance().setWsRuta(variables[0]);
                    Control.getInstance().setWsUsuario(variables[1]);
                    Control.getInstance().setWsContrasena(variables[2]);
                } else {
                    throw new Exception("No fue posible leer las variables de conexión para el webservices");
                }
                if (Control.getInstance().getWsRuta() == null || Control.getInstance().getWsRuta().equals("")
                        || Control.getInstance().getWsUsuario() == null || Control.getInstance().getWsUsuario().equals("")
                        || Control.getInstance().getWsContrasena() == null || Control.getInstance().getWsContrasena().equals("")) {
                    throw new Exception("No fue posible leer las variables de conexión para el webservices");
                }
                carga = true;
            } else {
                carga = false;
                throw new Exception("No fue posible leer las variables de conexión para el webservices");
            }
            //Serial receptor
            if (entrada.ready()) {
                Control.getInstance().setReceptorSerial(entrada.readLine().trim());
                if (Control.getInstance().getReceptorSerial() == null || Control.getInstance().getReceptorSerial().equals("")) {
                    throw new Exception("No fue posible leer el serial del archivo de configuración");
                }
                carga = true;
            } else {
                carga = false;
                throw new Exception("No fue posible leer el serial del archivo de configuración");
            }
            //Puerto serial
            if (entrada.ready()) {
                Control.getInstance().setSerialPuerto(entrada.readLine().trim());
                if (Control.getInstance().getSerialPuerto() == null || Control.getInstance().getSerialPuerto().equals("")) {
                    throw new Exception("No fue posible leer el puerto del archivo de configuración");
                }
                carga = true;
            } else {
                carga = false;
                throw new Exception("No fue posible leer el puerto del archivo de configuración");
            }
            //Tipo de Almacenamiento
            if (entrada.ready()) {
                switch (Integer.parseInt(entrada.readLine().trim())) {
                    case ALMACENAMIENTO_MEMORIA:
                        Control.getInstance().setAlamacenamiento(ALMACENAMIENTO_MEMORIA);
                        break;
                    case ALMACENAMIENTO_TEXTO:
                        Control.getInstance().setAlamacenamiento(ALMACENAMIENTO_TEXTO);
                        break;
                    default:
                        throw new Exception("No fue posible identificar el tipo de almacenamiento del archivo de configuración");
                }
                if (Control.getInstance().getAlamacenamiento() == null) {
                    throw new Exception("No fue posible leer el tipo de almacenamiento del archivo de configuración");
                }
                carga = true;
            } else {
                carga = false;
                throw new Exception("No fue posible leer el tipo de almacenamiento del archivo de configuración");
            }
            //Carga de la puerta de enlace (PROXY)
            if (entrada.ready()) {
                if (entrada.ready()) {
                    String text = entrada.readLine().trim();
                    if (text != null && text.length() > 0 && "_".equals(text)) {
                        System.setProperty("http.proxyHost", entrada.readLine().trim());
                    }
                    
                }
            }
            if (entrada.ready()) {
                if (entrada.ready()) {
                    String text = entrada.readLine().trim();
                    if (text != null && text.length() > 0 && "_".equals(text)) {
                        System.setProperty("http.proxyPort", entrada.readLine().trim());
                    }
                }
            }
            
            /**********************************************************
            * Datos de configuración integración con Brasystem Sifasa *
            ***********************************************************/
            //Variables de conexión WS
            if (entrada.ready()) {
                if (entrada.ready()) {
                    Control.getInstance().setWsEndpoint(entrada.readLine().trim());
                }
            } else {
                servicio.transaccion.sofasa.Log.getInstance().error("Configuración: ", "No fue posible leer las variables de conexión para el webservices", new Exception("No fue posible leer las variables de conexión para el webservices"));
                //SendEmailUtil.sendTextEmail("Prueba", "Prueba", "ajrodriguezsi@groupware.com.co");
                throw new Exception("No fue posible leer las variables de conexión para el webservices");
            }
            
            // WS AuthenticationKey
            if (entrada.ready()) {
                if (entrada.ready()) {
                    String wsAuthenticationKey = entrada.readLine().trim();
                    System.out.println("--wsAuthenticationKey: " + wsAuthenticationKey);
                    Control.getInstance().setWsAuthenticationKey(wsAuthenticationKey);
                }
            } else {
                servicio.transaccion.sofasa.Log.getInstance().error("Configuración: ", "No fue posible leer las variables de AuthenticationKey para el webservices", new Exception("No fue posible leer las variables de AuthenticationKey para el webservices"));
                throw new Exception("No fue posible leer las variables de AuthenticationKey para el webservices");
            }
            
            // WS events email
            if (entrada.ready()) {
                if (entrada.ready()) {
                    String emails = entrada.readLine().trim();
                    System.out.println("--emails: " + emails);
                    Control.getInstance().setWsEventsMail(emails);
                }
            } else {
                servicio.transaccion.sofasa.Log.getInstance().error("Configuración: ", "No fue posible leer las variables email", new Exception("No fue posible leer las variables email"));
               
            }
            
        } catch (FileNotFoundException ex) {
            throw new Exception("Error al intentar abrir el acrivo de configuración (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            throw new Exception("ERROR en la lectutra del archivo de configuración (" + ex.getMessage() + ").");
        } catch (Exception ex) {
            throw new Exception("No fue posible cargar la configuración para la operación (" + ex.getMessage() + ").");
        }
        return carga;
    }

}
