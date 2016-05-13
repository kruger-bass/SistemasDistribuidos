/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client RMI interface
 * @author Marmota
 */
public interface InterfaceCliente extends Remote{
    public void printer(String s) throws RemoteException;
}
