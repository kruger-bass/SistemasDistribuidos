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
 *
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
    
    boolean inUse = false;
    
    GraphServer gui;
    
    public ServidorServo() throws RemoteException{
    
        System.out.println("debug1");
        gui = new GraphServer(this);
        System.out.println("debug2");
        
    }
    
    
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
                                && aux.qtd>0){
                            
                                aux.qtd = aux.qtd - 1;
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
    
    @Override
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
                                && aux.quarto>0){
                            
                                aux.quarto = aux.quarto - 1;
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
    
    public void verify(String date) throws RemoteException{
        
        HashMap<String, Passagem> airfareList = airfareDateMap.get(date);
        
        Iterator it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                        }
    }
    
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
    public HashMap ClientVerifyAirfare(String date) throws RemoteException{
        System.out.println(date);
        return airfareDateMap.get(date);
    }
    
    @Override
    public HashMap ClientVerifyLodging(String date) throws RemoteException{
        System.out.println(date);
        return lodgingDateMap.get(date);
    }
    
    @Override
    public String testConnection() throws RemoteException {
        return "Bom Dia";
    }
    
    @Override
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
    
    public void checkNotification(Passagem p) throws RemoteException{
        
        HashMap<Passagem, InterfaceCliente> airfareClientList = airfareInterestMap.get((p.diaIda + p.mesIda + p.anoIda));
        
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
    
    public void checkNotification(Hospedagem h) throws RemoteException{
        
        HashMap<Hospedagem, InterfaceCliente> lodgingClientList = lodgingInterestMap.get((h.entrada));
        
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

    @Override
    public boolean requestService() throws RemoteException{
        
        if(inUse){
            return false;
        } else{
            inUse = true;
            return true;
        }
    }
    
    @Override
    public void finishService() throws RemoteException{
        
        inUse = false;
    }
    
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
                                && aux.qtd>0){
                                
                                existeIda = true;
                            }
                            
                        }
                        
        airfareList = airfareDateMap.get((p.diaVolta + p.mesVolta + p.anoVolta));
        
        it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                            
                            if(aux.origem.equals(p.origem)
                                && aux.destino.equals(p.destino)
                                && aux.qtd>0){
                                
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
