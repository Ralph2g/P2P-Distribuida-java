package Modelo;

import java.io.Serializable;

public class Mensaje implements Serializable{
    private final char separador = '$';
    
    private int id;
    private String nombreOrigen;
    private String nombreDestino;
    private String mensaje;
    
    public Mensaje(){
        this.id = 0;
        this.nombreOrigen = "";
        this.mensaje = "";
        this.nombreDestino = "";
    }
    
    public Mensaje(int id, String nombreOrigen, String nombreDestino, String mensaje){
        this.id = id;
        this.nombreOrigen = nombreOrigen;
        this.mensaje = mensaje;
        this.nombreDestino = nombreDestino;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNombreOrigen(String nombreOrigen){
        this.nombreOrigen = nombreOrigen;
    }
    
    public void setNombreDestino(String nombreDestino){
        this.nombreDestino = nombreDestino;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }
    
    public int getId(){
        return id;
    }
    
    public String getNombreDestino(){
        return nombreDestino;
    }
    
    public String getNombreOrigen(){
        return nombreOrigen;
    }
    
    public String getMensaje(){
        return mensaje;
    }
    
    public void imprimir(){
        System.out.print("Id mensaje: " + id + "\n");
        System.out.print("Origen: " + nombreOrigen + "\n");
        System.out.print("Destino: " + nombreDestino + "\n");
        System.out.print("Mensaje: " + mensaje + "\n");
    }
    
    public String getCadenaMensaje(){
        return id + "" + separador + nombreOrigen + separador + nombreDestino + separador + mensaje + separador;
    }
}