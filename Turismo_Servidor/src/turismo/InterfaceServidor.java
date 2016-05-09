/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public interface InterfaceServidor extends Remote{
    
    public HashMap ClientVerifyAirfare(String date) throws RemoteException;
    public String testConnection() throws RemoteException;
    public void removeAirfare(Passagem p) throws RemoteException;
}
