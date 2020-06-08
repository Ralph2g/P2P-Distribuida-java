package Modelo;

import java.io.Serializable;

public class Nodo implements Serializable{
    private String id;
    private String ip;//Ip del servidor
    private int puerto;//Puerto de servicio del servidor
    
    public Nodo() {}
    
    public Nodo(String id, String ip, int puerto) {
        this.id = id;
        this.ip = ip;
        this.puerto = puerto;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
    
    public String getId() {
        return id;
    }
    
    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }  
}
