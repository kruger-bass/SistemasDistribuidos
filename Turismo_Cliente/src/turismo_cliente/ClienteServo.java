/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo_cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Marmota
 */
public class ClienteServo extends UnicastRemoteObject implements InterfaceCliente{
    
    public ClienteServo(InterfaceServidor server) throws RemoteException{}
    
    public void NotificaEvento(String evento) throws RemoteException
    {
        
    }
}
