/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public class Ledger implements Serializable{
    
    
    //transaction ID, and packet with all info on transaction
     HashMap<Integer, MessagePacket> transactionList = new HashMap<Integer, MessagePacket>();
     
     //transaction ID, and pacote with all info on transaction
     HashMap<Integer, MessagePacket> transactionWaitingList = new HashMap<Integer, MessagePacket>();
     
     //portID, message packet
     HashMap<Integer, MessagePacket> userList = new HashMap<Integer, MessagePacket>();
     
     
     public Ledger(){
     }
     
     // Valida a transação.
     public void confirmTransaction(byte[] data, byte[] signature, PublicKey pk, int transID){
         boolean verified = GenSig.VerifySignature(data, signature, pk);
         if(verified){
            //transactionList.put(transID, transactionWaitingList.get(transID));
            //transactionWaitingList.remove(transID);
         } else{
             System.out.println("Error: ConfirmTransaction");
         }
     }
     
     //
     public void transactionConfirmed(int transID){
         
         transactionList.put(transID, transactionWaitingList.get(transID));
         transactionWaitingList.remove(transID);
     }
     
     // inclui transação a ser validada
     public void addTransaction(int transID, MessagePacket packet){
         transactionWaitingList.put(transID, packet);
     }
     
     // inclui usuário no BD
     public void addUser(MessagePacket packet){
         userList.put(packet.portID, packet);
     }
}
