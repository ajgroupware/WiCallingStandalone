/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion;

//import com.google.gson.Gson;
import servicio.objetos.Transaccion;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author rpalacic
 */
public class ComunicacionWS {
    
    private static final String URL = Control.getInstance().getWsRuta();
    private static final String USUARIO = Control.getInstance().getWsUsuario();
    private static final String CONTRASENA = Control.getInstance().getWsContrasena();
    
    public static boolean enviarTransacciones(List <Transaccion> transacciones) {
        boolean resp = false;
        try {
            String httpURL = URL + "rest/comunicacion/recibir-transacciones";
            Gson gson = new Gson();
            String transaccionesStr = gson.toJson(transacciones);
            String userpass = USUARIO + ":" + CONTRASENA;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            //Transaccion
            URL myurl = new URL(httpURL);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestProperty("transacciones", transaccionesStr);
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.replace("},{", "}\n{");
                resp = Boolean.valueOf(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }

    public static boolean validar() {
        boolean resp = false;
        try {
            String httpURL = URL + "rest/comunicacion/validar";
            //Transacion
            URL myurl = new URL(httpURL);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            String userpass = USUARIO + ":" + CONTRASENA;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.replace("},{", "}\n{");
                resp = Boolean.valueOf(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }
    
    public static boolean verificar(String serial) {
        boolean resp = false;
        try {
            String httpURL = URL + "rest/comunicacion/verificar";
            //Transacion
            URL myurl = new URL(httpURL);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            String userpass = USUARIO + ":" + CONTRASENA;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestProperty("serial", serial);
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.replace("},{", "}\n{");
                resp = Boolean.valueOf(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }
    
    public static boolean errorSerial(String serial, boolean estado) {
        boolean resp = false;
        try {
            String httpURL = URL + "rest/comunicacion/error-serial";
            //Transacion
            URL myurl = new URL(httpURL);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            String userpass = USUARIO + ":" + CONTRASENA;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestProperty("serial", serial);
            con.setRequestProperty("estado", (estado)?"1":"0");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.replace("},{", "}\n{");
                resp = Boolean.valueOf(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }
    
}
