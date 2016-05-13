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
 * Client's remote object
 * @author Marmota
 */
public class ClienteServo extends UnicastRemoteObject implements InterfaceCliente{
    
    GraphClient gui;
    InterfaceServidor server;
    HashMap<String, Passagem> airfareList = new HashMap<String, Passagem>();
    HashMap<String, Hospedagem> lodgingList = new HashMap<String, Hospedagem>();
    
    /**
     * Object constructor
     * @param server - the remote server with whom it communicates
     * @throws RemoteException 
     */
    public ClienteServo(InterfaceServidor server) throws RemoteException{
        gui = new GraphClient(this);
        this.server = server;
    }
    
    /**
     * Check airfare for a given date. Ignores all other info, for search purposes.
     * @param date
     * @throws RemoteException 
     */
    public void verifyAirfare(String date) throws RemoteException{   
        
        HashMap hash = server.ClientVerifyAirfare(date);

        if (hash == null){ // If no client whants to know about a airfare, do nothing!
            System.out.println("Nenhuma passagem encontrada para o dia selecionado");
        }
        else{
        airfareList = new HashMap<String, Passagem>(hash);
        Iterator it = airfareList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Passagem aux = (Passagem)pair.getValue();
                System.out.println(aux.origem + ": " + aux.destino + " - " + aux.diaIda + "/" + aux.mesIda + "/" + aux.anoIda + ". Preço: " + aux.preco + ". Disp.:" + aux.qtd);
            }
        }
    }
    /**
     * Check lodging for a given date. Ignores all other info, for search purposes.
     * @param date
     * @throws RemoteException 
     */
    public void verifyLodging(String date) throws RemoteException{
        HashMap hash = server.ClientVerifyLodging(date);
        if (hash == null){ // If no client whants to know about a airfare, do nothing!
            System.out.println("Nenhuma hospedagem encontrada para o dia selecionado");
        }
        else{
        lodgingList = new HashMap<String, Hospedagem>(hash);
        Iterator it = lodgingList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Hospedagem aux = (Hospedagem)pair.getValue();
                System.out.println(aux.cidade + ": " + aux.hotel + " - Entrada: " + aux.diaEntrada + "/" + aux.mesEntrada + "/" + aux.anoEntrada + " Saida: " + aux.diaSaida + "/" + aux.mesSaida + "/" + aux.anoSaida + ". Preço: " + aux.preco + ". Disp.:" + aux.quarto);
            }
        }
    }
    /**
     * Buy an airfare. Checks idaEvolta and them buys one-way or round-trip.
     * @param p
     * @param idaEvolta
     * @param volta
     * @throws RemoteException 
     */
    public void buyAirfare(Passagem p, boolean idaEvolta, int volta) throws RemoteException{
        while(server.requestService()){}
        
        if(!idaEvolta){
            server.removeAirfare(p);
        } else if(server.checkIdaEVolta(p)){
            server.removeAirfare(p);
            Passagem p2 = new Passagem();
            p2.origem = p.destino;
            p2.destino = p.origem;
            p2.diaIda = p.diaVolta;
            p2.mesIda = p.mesVolta;
            p2.anoIda = p.anoVolta;
            p2.preco = volta;
            p2.qtd = p.qtd;
            server.removeAirfare(p2);
        }else{
            System.out.println("Error: Airfare not found");
        }
        server.finishService();
    }
    
    /**
     * Buy an lodging option.
     * @param h
     * @throws RemoteException 
     */
    public void buyLodging(Hospedagem h) throws RemoteException{
        while(server.requestService2()){}
        
        System.out.println(h.quarto);
        server.removeLodging(h);
        
        server.finishService2();
    }
    
    /**
     * Connection tester
     * @throws RemoteException 
     */
    public void test() throws RemoteException{
            System.out.println(server.testConnection());
    }
    
    /**
     * Notification printer
     * @param s
     * @throws RemoteException 
     */
    @Override
    public void printer(String s) throws RemoteException{
        
        System.out.println(s);
    }
    
    /**
     * Register interest for a kind of airfare. When an update for that airfare happens, the server informs the client.
     * @param p
     * @throws RemoteException 
     */
    public void registerInterest(Passagem p) throws RemoteException {
                
        server.registerNotification(p, this);        
    }
    
    /**
     * Register interest for a kind of lodging. When an update for that lodginghappens, the server informs the client.
     * @param h
     * @throws RemoteException 
     */
    public void registerInterest(Hospedagem h) throws RemoteException {
        
        server.registerNotification(h, this);        
    }
    
}
