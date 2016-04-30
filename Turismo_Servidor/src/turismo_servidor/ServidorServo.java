/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo_servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Marmota
 */
public class ServidorServo extends UnicastRemoteObject implements InterfaceServidor{
    
    public ServidorServo() throws RemoteException{}
}
