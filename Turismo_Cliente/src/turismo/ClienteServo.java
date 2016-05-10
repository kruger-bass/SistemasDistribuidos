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
    HashMap<String, Hospedagem> lodgingList = new HashMap<String, Hospedagem>();
    
    public ClienteServo(InterfaceServidor server) throws RemoteException{
        System.out.println("debug");
        gui = new GraphClient(this);
        this.server = server;
    }
    
    public void verifyAirfare(String date) throws RemoteException{
        while(server.requestService()){}    
        
        airfareList = new HashMap<String, Passagem>(server.ClientVerifyAirfare(date));
            
        Iterator it = airfareList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Passagem aux = (Passagem)pair.getValue();
                System.out.println(aux.origem + ": " + aux.destino + " - " + aux.diaIda + "/" + aux.mesIda + "/" + aux.anoIda + ". Preço: " + aux.preco + ". Disp.:" + aux.qtd);
            }
       
            server.finishService();
            
    }
    
    public void verifyLodging(String date) throws RemoteException{
        while(server.requestService()){}
            
        lodgingList = new HashMap<String, Hospedagem>(server.ClientVerifyLodging(date));
            
        Iterator it = lodgingList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Hospedagem aux = (Hospedagem)pair.getValue();
                System.out.println(aux.cidade + ": " + aux.hotel + " - Entrada: " + aux.diaEntrada + "/" + aux.mesEntrada + "/" + aux.anoEntrada + " Saida: " + aux.diaSaida + "/" + aux.mesSaida + "/" + aux.anoSaida + ". Preço: " + aux.preco + ". Disp.:" + aux.quarto);
            }
            
        server.finishService();
    }
    
    public void buyAirfare(Passagem p, boolean idaEvolta, int volta) throws RemoteException{
        while(server.requestService()){}
        
        if(!idaEvolta){
            System.out.println(p.qtd);
            server.removeAirfare(p);
        } else if(server.checkIdaEVolta(p)){
            System.out.println("debugdebug");
            server.removeAirfare(p);
            Passagem p2 = new Passagem();
            p2.origem = p.destino;
            p2.destino = p.origem;
            p2.diaIda = p.diaVolta;
            p2.mesIda = p.mesVolta;
            p2.anoIda = p.anoVolta;
            p2.preco = volta;
            server.removeAirfare(p2);
        }else{
            System.out.println("Error: Airfare not found");
        }
        System.out.println("chegou no final");
        server.finishService();
    }
    
    public void buyLodging(Hospedagem h) throws RemoteException{
        while(server.requestService()){}
        
        System.out.println(h.quarto);
        server.removeLodging(h);
        
        server.finishService();
    }
    
    public void test() throws RemoteException{
            System.out.println(server.testConnection());
    }
    
    @Override
    public void printer(String s) throws RemoteException{
        
        System.out.println(s);
    }
    
    public void registerInterest(Passagem p) throws RemoteException {
        while(server.requestService()){}
        
        server.registerNotification(p, this);
        System.out.println("ok1");
        
        server.finishService();
    }
    
    public void registerInterest(Hospedagem h) throws RemoteException {
        while(server.requestService()){}
        
        server.registerNotification(h, this);
        System.out.println("ok2");
        
        server.finishService();
    }
    
}
