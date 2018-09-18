/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio.objetos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author rpalacic
 */
public class Transaccion {

    public Transaccion() {
    }

    public Transaccion(String serial, String valor, Date fechaHora) {
        this.serial = serial;
        this.valor = valor;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHora);
        this.fechaHora = df.format(cal.getTime());
    }
    
    private String serial;
    private String valor;
    private String fechaHora;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }    
        
}
