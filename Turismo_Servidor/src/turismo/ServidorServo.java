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
    
    HashMap<String, HashMap> lodgingDateMap = new HashMap<>();
    
    int counter = 0;
    
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
    }
        
    public void addLodging(Hospedagem h) throws RemoteException{
    
        if(lodgingDateMap.get(h.entrada) == null){
            
            HashMap<String, Hospedagem> lodgingList = new HashMap<String, Hospedagem>();
            lodgingList.put(("" + counter), h);
            counter++;
            airfareDateMap.put(h.entrada, lodgingList);
            
        } else{
            
            HashMap<String, Hospedagem> lodgingList = airfareDateMap.get(h.entrada);
            lodgingList.put(("" + counter), h);
            counter++;
            
        }
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
    
    public void verify(String date) throws RemoteException{
        
        HashMap<String, Passagem> airfareList = airfareDateMap.get(date);
        
        Iterator it = airfareList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Passagem aux = (Passagem)pair.getValue();
                            System.out.println(aux.origem + ": " + aux.qtd);
                        }
    }
    
    @Override
    public HashMap ClientVerifyAirfare(String date) throws RemoteException{
        System.out.println(date);
        return airfareDateMap.get(date);
    }
    
    @Override
    public String testConnection() throws RemoteException {
        return "Hello";
    }
}
