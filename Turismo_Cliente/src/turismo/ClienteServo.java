/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marmota
 */
public class ClienteServo extends UnicastRemoteObject implements InterfaceCliente{
    
    GraphClient gui;
    InterfaceServidor server;
    HashMap<String, Passagem> airfareList = new HashMap<String, Passagem>();
    
    public ClienteServo(InterfaceServidor server) throws RemoteException{
        System.out.println("debug");
        gui = new GraphClient(this);
        this.server = server;
    }
    
    public void verifyAirfare(String date) throws RemoteException{
            
        airfareList = new HashMap<String, Passagem>(server.ClientVerifyAirfare(date));
            
        Iterator it = airfareList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Passagem aux = (Passagem)pair.getValue();
                System.out.println(aux.origem);
            }
            
    }
    
    public void test() throws RemoteException{
            System.out.println(server.testConnection());
    }
}
