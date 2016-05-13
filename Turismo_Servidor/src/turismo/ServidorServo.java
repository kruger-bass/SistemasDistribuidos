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

/**
 * Server's Remote Object
 * @author Marmota
 */
public class ServidorServo extends UnicastRemoteObject implements InterfaceServidor{
    
    HashMap<String, HashMap> airfareDateMap = new HashMap<String, HashMap>();
    
    HashMap<String, HashMap> lodgingDateMap = new HashMap<String, HashMap>();
    
    HashMap<String, HashMap> airfareInterestMap = new HashMap<String, HashMap>();
    
    HashMap<String, HashMap> lodgingInterestMap = new HashMap<String, HashMap>();
    
    int counter = 0;
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;
    
    boolean inUseP = false;
    boolean inUseH = false;
    
    GraphServer gui;
    
    /**
     * Object constructor
     * @throws RemoteException 
     */
    public ServidorServo() throws RemoteException{
    
        System.out.println("debug1");
        gui = new GraphServer(this);
        System.out.println("debug2");
        
    }
    
    /**
     * Add airfare method. 
     * @param p an airfare
     * @throws RemoteException 
     */
    public void addAirfare(Passagem p) throws RemoteException{
    
        if(airfareDateMap.get((p.diaIda + p.mesIda + p.anoIda)) == null){
            
            HashMap<String, Passagem> airfareList = new HashMap<String, Passagem>();
            airfareList.put(("" + counter), p);
            counter++;
            airfareDateMap.put((p.diaIda + p.mesIda + p.anoIda), airfareList);
            
        } else{
            
            HashMap<String, Passagem> airfareList = airfareDateMap.get((p.diaIda + p.mesIda + p.anoIda));
            airfareList.put(("" + counter), p);
            counter++;
            
        }
        
        checkNotification(p);
    }
        
    /**
     * add Lodging option
     * @param h a lodging
     * @throws RemoteException 
     */
    public void addLodging(Hospedagem h) throws RemoteException{
    
        if(lodgingDateMap.get(h.entrada) == null){
            
            HashMap<String, Hospedagem> lodgingList = new HashMap<String, Hospedagem>();
            lodgingList.put(("" + counter2), h);
            counter2++;
            lodgingDateMap.put(h.entrada, lodgingList);
            
        } else{
            
            HashMap<String, Hospedagem> lodgingList = lodgingDateMap.get(h.entrada);
            lodgingList.put(("" + counter2), h);
            counter2++;
            
        }
        
        checkNotification(h);
    }

    /**
     * Remove Airfare method, called when an airfare is bought.
     * @param p
     * @throws RemoteException 
     */
    public void removeAirfare(Passagem p) throws RemoteException{
        
        if(airfareDateMap.get((p.diaIda + p.mesIda + p.anoIda)) == null){
            
            System.out.println("Error: Could not find specified airfare");
            
        } else{
            
            HashMap<String, Passagem> airfareList = airfareDateMap.get((p.diaIda + p.mesIda + p.anoIda));
            boolean removed = false;
            
            Iterator it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            
                            if(aux.origem.equals(p.origem)
                                && aux.destino.equals(p.destino)
                                && aux.preco == p.preco
                                && aux.qtd >= p.qtd){
                            
                                aux.qtd -= p.qtd;
                                System.out.println("Removida Passagem");
                                removed = true;
                                break;
                                
                            }
                        }
            if(!removed){
                System.out.println("Error: Not enough airfares left");
            }
        }
        
    }
    
    /**
     * remove Lodging method. Called when a lodge is bought
     * @param h
     * @throws RemoteException 
     */
    public void removeLodging(Hospedagem h) throws RemoteException{
        
        if(lodgingDateMap.get((h.entrada)) == null){
            
            System.out.println("Error: Could not find specified lodging");
            
        } else{
            
            HashMap<String, Hospedagem> lodgingList = lodgingDateMap.get((h.entrada));
            boolean removed = false;
            
            Iterator it = lodgingList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Hospedagem aux = (Hospedagem)pair.getValue();
                            
                            if(aux.cidade.equals(h.cidade)
                                && aux.hotel.equals(h.hotel)
                                && aux.preco == h.preco
                                && aux.quarto>= h.quarto){
                            
                                aux.quarto -= h.quarto;
                                System.out.println("Removida Hospedagem");
                                removed = true;
                                break;
                                
                            }
                        }
            if(!removed){
                System.out.println("Error: Not enough lodgings left");
            }
        }
        
    }
    
    /**
     * server method that checks available airfares
     * @param date
     * @throws RemoteException 
     */
    public void verify(String date) throws RemoteException{
        
        HashMap<String, Passagem> airfareList = airfareDateMap.get(date);
        
        Iterator it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                        }
    }
    
    /**
     * Server method that checks available lodging
     * @param date
     * @throws RemoteException 
     */
    public void verifyLodging(String date) throws RemoteException{
    
        HashMap<String, Hospedagem> lodgingList = lodgingDateMap.get(date);
        
        Iterator it = lodgingList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Hospedagem aux = (Hospedagem)pair.getValue();
                            System.out.println(aux.cidade + ": " + aux.quarto);
                        }
    }
    
    @Override
    /**
     * Remote method. Returns a hashmap with airfares that looks alike the ones requested (from, to, date...)
     */
    public HashMap ClientVerifyAirfare(String date) throws RemoteException{
        System.out.println(date);
        return airfareDateMap.get(date);
    }
    
    @Override
    /**
     * Remote method. Returns a hashmap with lodging that looks alike the ones requested (city, hotel, date...)
     */
    public HashMap ClientVerifyLodging(String date) throws RemoteException{
        System.out.println(date);
        return lodgingDateMap.get(date);
    }
    
    @Override
    /**
     * Connection tester
     */
    public String testConnection() throws RemoteException {
        return "Bom Dia";
    }
    
    @Override
    /**
     * RMI method that register a client interest in an airfare
     */
    public void registerNotification(Passagem p, InterfaceCliente client) throws RemoteException {
        
        if(airfareInterestMap.get((p.diaIda + p.mesIda + p.anoIda)) == null){
            
            HashMap<Passagem, InterfaceCliente> airfareClientList = new HashMap<Passagem, InterfaceCliente>();
            airfareClientList.put(p, client);
            airfareInterestMap.put((p.diaIda + p.mesIda + p.anoIda), airfareClientList);
            System.out.println("regp");
            
        } else{
            
            HashMap<Passagem, InterfaceCliente> airfareClientList = airfareInterestMap.get((p.diaIda + p.mesIda + p.anoIda));
            airfareClientList.put(p, client);
            System.out.println("regp2");
            
        }
    }
    
    @Override
    /**
     * RMI method that register a client interest in a lodging
     */
    public void registerNotification(Hospedagem h, InterfaceCliente client) throws RemoteException {
    
        if(lodgingInterestMap.get(h.entrada) == null){
            
            HashMap<Hospedagem, InterfaceCliente> lodgingClientList = new HashMap<Hospedagem, InterfaceCliente>();
            lodgingClientList.put(h, client);
            lodgingInterestMap.put(h.entrada, lodgingClientList);
            System.out.println("regl");
            
        } else{
            
            HashMap<Hospedagem, InterfaceCliente> lodgingClientList = lodgingInterestMap.get(h.entrada);
            lodgingClientList.put(h, client);
            System.out.println("regl2");
            
        }
        
    }
    
    /**
     * Server method that checks if someone whants to be notified about an airfare
     * @param p
     * @throws RemoteException 
     */
    public void checkNotification(Passagem p) throws RemoteException{
        
        HashMap<Passagem, InterfaceCliente> airfareClientList = airfareInterestMap.get((p.diaIda + p.mesIda + p.anoIda));
        
        if (airfareClientList == null){ // If no client whants to know about a airfare, do nothing!
            
        }
        else {    
        Iterator it = airfareClientList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getKey();
                            System.out.println(aux.origem + ": " + aux.destino);
                            
                            System.out.println(p.preco +":" + aux.preco);
                            if(p.origem.equals(aux.origem)
                            && p.destino.equals(aux.destino)
                            && p.preco<aux.preco
                            && System.currentTimeMillis()<aux.expiration){
                                
                                InterfaceCliente c = (InterfaceCliente)pair.getValue();
                                c.printer("Existe a passagem requisitada por um preço melhor!");
                            }
                        }
        }
        
    }
    
    /**
     * Server method that checks if someone whants to be notified about a lodging
     * @param h
     * @throws RemoteException 
     */
    public void checkNotification(Hospedagem h) throws RemoteException{
        
        HashMap<Hospedagem, InterfaceCliente> lodgingClientList = lodgingInterestMap.get((h.entrada));
        
        if (lodgingClientList == null){ // If no client whants to know about a lodging, do nothing!
            
        }
        else {    
        Iterator it = lodgingClientList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Hospedagem aux = (Hospedagem)pair.getKey();
                            System.out.println(aux.cidade + ": " + aux.hotel);
                            
                            System.out.println(h.preco +":" + aux.preco);
                            if(h.cidade.equals(aux.cidade)
                            && h.hotel.equals(aux.hotel)
                            && h.preco<aux.preco
                            && System.currentTimeMillis()<aux.expiration){
                                
                                InterfaceCliente c = (InterfaceCliente)pair.getValue();
                                c.printer("Existe a hospedagem requisitada por um preço melhor!");
                            }
                        }
        }
    }

    @Override
    /**
     * Server access control. blocks concurrent access to server methods.
     */
    public boolean requestService() throws RemoteException{
        
        if(inUseP){
            return false;
        } else{
            inUseP = true;
            return true;
        }
    }
    
        /**
     * Server access control. blocks concurrent access to server methods.
     */
    public boolean requestService2() throws RemoteException{
        
        if(inUseH){
            return false;
        } else{
            inUseH = true;
            return true;
        }
    }
    
    @Override
    /**
     * Server access control release. Releases the server to other users.
     */
    public void finishService() throws RemoteException{
        
        inUseP = false;
    }
    
    
    @Override
    /**
     * Server access control release. Releases the server to other users.
     */
    public void finishService2() throws RemoteException{
        
        inUseH = false;
    }
    
    /**
     * Roundtrip method. used when a roundtrip airfare is requested, to check availability both ways
     * @param p
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean checkIdaEVolta(Passagem p) throws RemoteException{
    
        boolean existeIda = false;
        boolean existeVolta = false;
        
        HashMap<String, Passagem> airfareList = airfareDateMap.get((p.diaIda + p.mesIda + p.anoIda));
        
        Iterator it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                            
                            if(aux.origem.equals(p.origem)
                                && aux.destino.equals(p.destino)
                                && aux.qtd>= p.qtd){
                                
                                existeIda = true;
                            }
                            
                        }
                        
        airfareList = airfareDateMap.get((p.diaVolta + p.mesVolta + p.anoVolta));
        
        it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                            
                            if(aux.origem.equals(p.destino)
                                && aux.destino.equals(p.origem)
                                && aux.qtd>=p.qtd){
                                
                                existeVolta = true;
                            }
                            
                        }
                        
         if(existeIda && existeVolta){
             return true;
         }else{
             return false;
         }
    
    }
    

}
