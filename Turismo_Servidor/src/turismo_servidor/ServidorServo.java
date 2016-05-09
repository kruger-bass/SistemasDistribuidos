/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo_servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public class ServidorServo extends UnicastRemoteObject implements InterfaceServidor{
    
    HashMap<String, HashMap> airfareDateMap = new HashMap<String, HashMap>();
    
    HashMap<String, HashMap> lodgingDateMap = new HashMap<String, HashMap>();
    
    int counter = 0;
    
    public ServidorServo() throws RemoteException{}
    
    
    public void addAirfare(Passagem p){
    
        if(airfareDateMap.get(p.dataIda) == null){
            
            HashMap<String, Passagem> airfareList = new HashMap<String, Passagem>();
            airfareList.put(("" + counter), p);
            counter++;
            airfareDateMap.put(p.dataIda, airfareList);
            
        } else{
            
            HashMap<String, Passagem> airfareList = airfareDateMap.get(p.dataIda);
            airfareList.put(("" + counter), p);
            counter++;
            
        }
    }
        
    public void addLodging(Hospedagem h){
    
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
    
    public void buscaPassagem (Passagem p)
    {
        
    }
    
    public void buscaHospedagem (Hospedagem h)
    {
        
    }
    
    public int compraPassagem(Passagem p)
    {
        
        return ;
    }
    
    public int compraHospedagem(Hospedagem h)
    {
        
        return ;
    }
    
    public void interessePassagem(Passagem p)
    {
        
    }
    
    public void interesseHospedagem(Hospedagem h)
    {
        
    }
}
