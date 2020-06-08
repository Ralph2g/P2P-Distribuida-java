package Servidores;

import static Servidores.ServidorRMI.RUTA;
import static Servidores.ServidorRMI.pto;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorFlujo extends Thread{
    public static ServerSocket s1;
    public static int ptoRMI;
    public static int ptoFlujo;
    public static final String RUTA = "Carpetas";
    public static final int TAM_BUFFER = 1024;
    
    public ServidorFlujo(){}
    
    public void inicializaServer (int puerto) throws IOException{
        ptoFlujo=puerto;
        ptoRMI=puerto-100;
    }
    
    @Override
    public void run(){
        try {
          MulticastMensajes mess = new MulticastMensajes ();
          ServerSocket s=new ServerSocket(ptoFlujo);
            for(;;){ 
                //Esperamos una conexión
                Socket cl = s.accept();
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                String nombre;
                long enviados;

                nombre = dis.readUTF();
                File file = new File (RUTA+ptoRMI+"/"+nombre);
                String archivo = file.getAbsolutePath();
                int n;
                long tam = file.length(); //Tamaño
                int numByte = 1024;
                DataInputStream disSend = new DataInputStream(new FileInputStream(archivo));
                dos.writeLong(tam);
                dos.flush();
                System.out.println("Nombre: "+nombre);
                System.out.println("Tamanio: "+tam);
                byte[] b = new byte[numByte];
                int porcentaje = 0, bitRes=numByte, aux=10;
                enviados = 0;
                while(enviados < tam){
                    if ((tam-enviados) < numByte){
                        bitRes = (int)(tam-enviados);
                    }
                    n = disSend.read(b, 0, bitRes);
                    dos.write(b,0,n);
                    dos.flush();
                    enviados = enviados + n;
                    porcentaje = (int)(enviados*100/tam);
                    if (porcentaje >= aux){
                        mess.enviaMensaje("<"+ptoRMI+">"+nombre+": porcentaje enviado de "+porcentaje+"%\n");
                        aux += 10;
                    }
                }//while
                disSend.close ();
                dis.close();
                dos.close ();
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }        
    }
}