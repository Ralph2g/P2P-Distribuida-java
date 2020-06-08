package Servidores;

import Modelo.Nodo;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorRMI extends Thread implements Funciones{
    public static final String RUTA = "Carpetas";
    public static int pto;
    
    public ServidorRMI (){}
    
    @Override
    public void run (){
        try {
            System.out.println("Puerto RMI: "+pto);
            java.rmi.registry.LocateRegistry.createRegistry(pto);
	    ServidorRMI obj = new ServidorRMI();
	    Funciones stub = (Funciones) UnicastRemoteObject.exportObject(obj, 0);
	    Registry registry = LocateRegistry.getRegistry(pto);
	    registry.bind("Funciones", stub);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void setPto  (int num){
        this.pto = num;
    }
    
    @Override
    public int buscarArchivo(String nombre, int puertoSolicitante, ArrayList <Nodo> nod) throws RemoteException, AccessException{
        //if (puertoSolicitante != pto){
            MulticastMensajes cl = new MulticastMensajes ();
            File carpeta = new File(RUTA+pto+"/");
            File[] listOfFiles = carpeta.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().equals(nombre)){
                        try {
                            cl.enviaMensaje("<"+pto+">"+"Archivo "+nombre+" localizado en el nodo.\n");
                            return pto;//Retorno al puerto donde esta el archivo
                        } catch (IOException ex) {
                            Logger.getLogger(ServidorRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } 
           
        boolean tengoNodoMayor=false;
        for (int i = 0; i < nod.size(); i++){
            if (((pto+1)==nod.get(i).getPuerto())){
                tengoNodoMayor = true;
                break;
            }
        }
        Registry registry = null;
        int sigPto=0;
        String ipSiguiente="";
        boolean continuaCiclo=true;
        if (tengoNodoMayor){
            for (int k = 0; k < nod.size(); k++){
                if ((pto+1) == nod.get(k).getPuerto()){
                    ipSiguiente = nod.get(k).getIp(); 
                    sigPto = pto+1;	
                    break;
                }
            }
            if (sigPto==puertoSolicitante){
                continuaCiclo=false;
            }
        }else{
            boolean isPtoMayor = true;
            for (int n = 0; n < nod.size(); n++){
                if (pto < nod.get(n).getPuerto()){
                    isPtoMayor=false;
                }
            }
            if (isPtoMayor){
                if ((9000==puertoSolicitante)||((pto+1)==puertoSolicitante)){
                    continuaCiclo=false;
                }
            }else{
                if ((pto+1)==puertoSolicitante){
                    continuaCiclo=false;
                }
            }
            for (int k = 0; k < nod.size(); k++){
               if (9000 == nod.get(k).getPuerto()){
                   ipSiguiente = nod.get(k).getIp();
                   sigPto = 9000;	
                   break;
               }
           }   
        }
        if (continuaCiclo){
            try {
                cl.enviaMensaje("<"+pto+">"+"Archivo "+nombre+" no localizado, preguntado al nodo "+sigPto+".\n");
            } catch (IOException ex) {
                Logger.getLogger(ServidorRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
            registry = LocateRegistry.getRegistry(ipSiguiente, sigPto);	
            //también puedes usar getRegistry(String host, int port
                Funciones stub = null;
            try {
                stub = (Funciones) registry.lookup("Funciones");
            } catch (NotBoundException ex) {
                Logger.getLogger(ServidorRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
                int n= stub.buscarArchivo(nombre, puertoSolicitante, nod);
                return n;
        }
        try {
            cl.enviaMensaje("<"+sigPto+">"+"Archivo "+nombre+" solicitado por "+puertoSolicitante+ " no localizado.\n");
        } catch (IOException ex) {
            Logger.getLogger(ServidorRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public void mensaje(String message) {
        System.out.println(message);
    }

    @Override
    public void porcentaje(int porcentaje) {
        System.out.println(porcentaje);
    }
}
