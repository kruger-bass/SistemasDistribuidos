/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Server RMI interface
 * @author Marmota
 */
public interface InterfaceServidor extends Remote{
    
    public HashMap ClientVerifyAirfare(String date) throws RemoteException;
    public HashMap ClientVerifyLodging(String date) throws RemoteException;
    public String testConnection() throws RemoteException;
    public void removeAirfare(Passagem p) throws RemoteException;
    public void removeLodging(Hospedagem h) throws RemoteException;
    public void registerNotification(Passagem p, InterfaceCliente client) throws RemoteException;
    public void registerNotification(Hospedagem h, InterfaceCliente client) throws RemoteException;
    public boolean requestService() throws RemoteException;
    public void finishService() throws RemoteException;
    public boolean requestService2() throws RemoteException;
    public void finishService2() throws RemoteException;
    public boolean checkIdaEVolta(Passagem p) throws RemoteException;
}
