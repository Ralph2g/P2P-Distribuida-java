package Servidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


public class MulticastMensajes{
    private final int PUERTO = 4000;    
    private MulticastSocket s;
    private InetAddress gpo;
    
    public final int TAM_BUFFER = 100;
    
    public MulticastMensajes (){
        try{
            s = new MulticastSocket (PUERTO);
            s.setReuseAddress(true);
            s.setTimeToLive(1);
            gpo = InetAddress.getByName("228.1.1.1");
            s.joinGroup(gpo);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void enviaMensaje (String msj) throws UnknownHostException, IOException{
        byte []b = msj.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, gpo, 9999);
            s.send (p);
    }
}
