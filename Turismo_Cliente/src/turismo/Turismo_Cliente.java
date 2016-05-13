/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Client main class
 * @author kruger
 */
public class Turismo_Cliente {

    /**
     * Client main. Register the client on RMi nameService and check if there's a server online.
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        try{
            int port = 2345;
            
            Registry referenciaServicoNomes = LocateRegistry.getRegistry("localhost", port);
            
            InterfaceServidor server = (InterfaceServidor)referenciaServicoNomes.lookup("server");
            
            ClienteServo interfaceCliente = new ClienteServo(server);
            System.out.println("Iniciado");
            
        } catch(RemoteException e){
            System.out.println(e);
            System.exit(0);
        } catch(NotBoundException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    
}
