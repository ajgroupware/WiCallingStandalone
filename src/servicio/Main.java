/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import propio.Log;
import servicio.transaccion.ComunicacionWS;
import servicio.transaccion.Control;
import servicio.objetos.Transaccion;

/**
 *
 * @author systemtech
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            String sSistemaOperativo = System.getProperty("os.name");
//            System.out.println(sSistemaOperativo);
//            String OS = System.getProperty("os.name").toLowerCase();
//            String OSArch = System.getProperty("os.arch").toLowerCase();
//            String OSVersion = System.getProperty("os.version").toLowerCase();
            if (Control.getInstance().cargarConfiguracion()) {
//                pruebaEnviarTransaccion();
//                pruebaVerificar();
//                pruebaValidar();
//                pruebaErrorSerial();
                Control.getInstance().hiloComunicacionSerialIniciar();
                Control.getInstance().hiloComunicacionWsIniciar();
                Control.getInstance().hiloComunicacionBrasystemIniciar();
                Control.getInstance().hiloLimpiarLogsIniciar();
                Log.getInstance().suceso("Inicio del servicio", "Se inicio el servicio de manera exitosa");
            } else {
                Log.getInstance().suceso("FALLO en el inicio del servicio", "Ocurrio un FALLO desconocido que no permitio iniciar el servicio");
            }
        } catch (Exception ex) {
            Log.getInstance().error("ERROR en la carga del servicio", "Ocurrio un ERROR que no permitio iniciar la carga del servicio", ex);
        }
    }

    private static void pruebaEnviarTransaccion() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Transaccion> lista = new ArrayList();
        Transaccion t;
        //LLAMADOS CON RESPUESTA
        //LLAMADO
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-25-193-65-93");
        lista.add(t);
        //CANCELADO
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-25-193-67-91");
        lista.add(t);
        //CALIFICACION
        //EXCELENTE
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-96-59-33-188");
        lista.add(t);
        //BUENO
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-96-59-34-187");
        lista.add(t);
        //REGULAR
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-96-59-35-186");
        lista.add(t);
        //MALO
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-96-59-36-185");
        lista.add(t);
        //PESIMO
        t = new Transaccion();
        t.setFechaHora(df.format(new Date()));
        t.setSerial("prueba");
        t.setValor("85-170-1-131-3-96-59-37-184");
        //Llenado de lista
        lista.add(t);
        System.out.println("enviarTransaccion: " + ComunicacionWS.enviarTransacciones(lista));
    }

    private static void pruebaValidar() {
        System.out.println("validar: " + ComunicacionWS.validar());
    }

    private static void pruebaVerificar() {
        System.out.println("verificar: " + ComunicacionWS.verificar("prueba"));
    }

    private static void pruebaErrorSerial() {
        System.out.println("errorSerial: " + ComunicacionWS.errorSerial("prueba", true));
    }

}
