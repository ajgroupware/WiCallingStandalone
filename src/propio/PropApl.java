/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package propio;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author rpalacic
 */
public class PropApl {
    private static PropApl instance = null;
    private Properties dbProps = null;
    
    private PropApl() {
        String sError=null;
        InputStream is = getClass().getResourceAsStream("apl.properties");
        dbProps = new Properties();
        try{
            dbProps.load(is);
            System.out.println("Carga propiedas de apl exitosa..."); 
        }catch(Exception e){
            sError = "No se puede leer el archivo de propiedades. Asegurese que el archivo apl.properties este en el CLASSPATH";
            System.err.println(e.getMessage() + " :> " + sError); 
        }
    }
    public static PropApl getInstance() { 
        if (instance==null) {
            instance = new PropApl();
        }
        return instance;
    }  
    /**
     * Obtiene el valor de un parametro
     */
    public String get(String param){
        if (dbProps==null){
            System.err.println("No pudo cargarse el archivo de propiedades."); 
            return ""; 
        }
        String str = dbProps.getProperty(param);
        if (str==null) {
            str="";
        }
        return str; 
    }
    
    public void set(String param, String val){
        if (dbProps!=null){
            dbProps.setProperty(param, val);
        }
    }
}
