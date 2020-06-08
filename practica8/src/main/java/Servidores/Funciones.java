package Servidores;

import Modelo.Nodo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Funciones extends Remote{
    public void mensaje(String message) throws RemoteException;
    public void porcentaje(int porcentaje) throws RemoteException;
    public int buscarArchivo(String nombre, int puertoSolicitante, ArrayList <Nodo> nod) throws RemoteException;
}