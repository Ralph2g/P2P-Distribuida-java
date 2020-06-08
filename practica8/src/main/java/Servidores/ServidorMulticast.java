package Servidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import Modelo.Mensaje;

//El servidor Este servidor básicamente se encargará de crear 
//un hilo, el cual anunciará cada 5 segundos el puerto de servicio del servidor de flujo. La dirección de grupo multicast que usará será la “228.1.1.1”
public class ServidorMulticast {
    private final String IP = "228.1.1.1";
    private final int PUERTO = 3000;
    
    private MulticastSocket cl;
    private DatagramPacket packet;
    private byte b[];
    private InetAddress grupo = null;
    
    public final int TAM_BUFFER = 100;
    public final String INICIO         = "<inicio>";
    public final String FIN            = "<fin>";
    
    public final static  int DESCONOCIDO_ID    = 0;
    public final static  int INICIO_ID         = 1;
    public final static  int FIN_ID            = 2;
    
    /*
     * Constructor de este servidor
     */
    private ServidorMulticast() {
        try{
            cl = new MulticastSocket(PUERTO);
            System.out.println("Cliente conectado desde: " + cl.getLocalPort());
            cl.setReuseAddress(true);
            cl.setTimeToLive(1);
            grupo = InetAddress.getByName(IP);
            cl.joinGroup(grupo);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /*
     * Holder para poder invocar el servidor haciendo una instancia de él.
     */
    private static class ServidorHolder {
        private static final ServidorMulticast INSTANCE = new ServidorMulticast();
    }
    
    /*
     * Constructor  de este servidor mediante el holder
     */
    public static ServidorMulticast getInstance() {
        return ServidorHolder.INSTANCE;
    }
    
    /*
     * Metodo que anuncia el puerto 
     * String nombreOrigen puerto a anunciar
    */
    public void anunciar(String nombreOrigen) throws IOException{
        String msj = INICIO + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    /*
     * Metodo que anuncia la salida del anillo de un nodo
     * @param nombreOrigen puerto a anunciar salida
     */
    public void salirDelAnillo(String nombreOrigen) throws IOException{
        String msj = FIN + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    /*
     * Método que recibe un mensaje desde el cliente en forma de datagrama 
     */
    public Mensaje recibe() throws IOException{
        DatagramPacket packet = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
        cl.receive(packet);
        String mensaje = new String(packet.getData());
        return getMensaje(mensaje);
    }
    
    /*
     * Método para obtener un mensaje desde el cliente para obtener   
     * su etiqueta y categorizarlo como nodo de inicio o final
     * @param mensaje mensaje a recibir
     */
    private Mensaje getMensaje(String mensaje){
        Mensaje msj = new Mensaje();
        
        switch(getEtiqueta(mensaje)){
            case INICIO:
                msj.setId(INICIO_ID);
                msj.setNombreOrigen(getNodoInicio(mensaje));
                break;
                
            case FIN:
                msj.setId(FIN_ID);
                msj.setNombreOrigen(getNodoFin(mensaje));
                break;
            default:
                msj.setId(DESCONOCIDO_ID);
                break;
        }
        
        return msj;       
    }
    
    /*
     * Método que recibe la etiqueta <inicio> o <fin> del mensaje
     * @param mensaje mensaje a recibir que contiene la etiqueta
     */
    private String getEtiqueta(String mensaje){
        int tam = mensaje.length(), i = 0;       
        char c;
        
        String etiqueta = "";
        
        if (mensaje.charAt(0) == '<') {
            while ((c = mensaje.charAt(i)) != '>' && i < tam) {
                etiqueta += c;
                i++;
            }
            if (c == '>'){
                etiqueta += c;
            }
        }
        
        return etiqueta;
    }
    
    /*
     * Método que establece el nodo de inicio mediante un mensaje
     * @param mensaje mensaje a establecer inicio
     */
    private String getNodoInicio(String mensaje){
        return mensaje.substring(INICIO.length()).trim();
    }
    
    /*
     * Método que establece el nodo final mediante un mensaje
     * @param mensaje mensaje a  establecer final
     */
    private String getNodoFin(String mensaje){
        return mensaje.substring(FIN.length()).trim();
    }
}
