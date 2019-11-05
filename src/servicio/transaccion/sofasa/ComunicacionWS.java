/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.transaccion.sofasa;

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
import servicio.transaccion.Control;

/**
 *
 * @author rpalacic
 */
public class ComunicacionWS {
    
    private static final String URL = Control.getInstance().getWsEndpoint();
        
    /*
    public static boolean enviarTransaccionesBrasystem(List <Transaccion> transacciones) {
        boolean resp = false;
        try {
            if (transacciones != null) {
                
                for (Transaccion transaccion : transacciones) {
                    sendToBrasystem(transaccion.getValor(), transaccion.getFechaHora(), "");
                }
            }
            
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }
    */
    
    public static boolean enviarTransaccionesBrasystem(Transaccion transaccion) {
        boolean resp = true;
        try {
            if (transaccion != null) {
                String wsAuthenticationKey = Control.getInstance().getWsAuthenticationKey();
                ModelApiResponse response = sendToBrasystem(transaccion.getValor(), transaccion.getFechaHora(), wsAuthenticationKey);
                if (response.getCode().intValue() == 200) {
                    
                } else {
                    resp = false;
                    //resp = true;
                }
            }
            
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return resp;
    }
    
    private static ModelApiResponse sendToBrasystem (String identification, String registerDate, String authenticationKey) {
        System.out.println("--sendToBrasystem");
        ModelApiResponse response = new ModelApiResponse();
        //String uri = URL + "api/v1/Integration/InitiateCall";
        String uri = URL;
        URL url;
        try {
            // 1. URL
            url = new URL(uri);

            // 2. Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 3. Specify GET method
            conn.setRequestMethod("POST");

            // 4. Set the headers
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("Authorization", "Bearer " + token);

            conn.setDoOutput(true);
            /*
            String input = "{\"identification\":\"" + identification + "\"" +
                    " , \"registerDate\":\"" + registerDate + "\"" +
                    " , \"authenticationKey\":\"" + authenticationKey + "\"" +
                    "}";
            */
            String input = "{\"code\":\"" + identification + "\"" +
                    " , \"date\":\"" + registerDate + "\"" +
                    " , \"key\":\"" + authenticationKey + "\"" +
                    "}";
            
            System.out.println("--json : " + input);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            os.close();

            // 5. Get the response
            int responseCode = conn.getResponseCode();
            System.out.println("--Sending 'POST' request to URL : " + url);
            System.out.println("--Response Code : " + responseCode);
            response.setCode(responseCode);
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
                String inputLine;
                StringBuffer responseMsg = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseMsg.append(inputLine);
                }
                in.close();

                // 6. Print result
                System.out.println(responseMsg.toString());
                response.setMessage(responseMsg.toString());
            } else {
                response.setMessage("Error");
            }

        } catch (MalformedURLException e) {
            response.setMessage("Error: MalformedURL");
            response.setCode(500);
            e.printStackTrace();
        } catch (IOException e) {
            response.setMessage("Error");
            response.setCode(500);
            e.printStackTrace();
        }

        return response;
    }

}
