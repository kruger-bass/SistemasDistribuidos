/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Marmota
 */
public interface InterfaceCliente extends Remote{
    public void printer(String s) throws RemoteException;
}
