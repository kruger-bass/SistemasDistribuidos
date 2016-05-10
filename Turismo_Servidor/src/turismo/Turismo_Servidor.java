/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author kruger
 */
public class Turismo_Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            int port = 2345;
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(port);
            
            ServidorServo interfaceServer = new ServidorServo();
            
            referenciaServicoNomes.rebind("server", interfaceServer);
            System.out.println("Iniciado");
            
        } catch(RemoteException e){
            System.exit(0);
        }
    }
    
}
