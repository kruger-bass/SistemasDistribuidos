/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public class Ledger {
    
    //transaction ID, and transaction
     HashMap<Integer, Transaction> transactionList = new HashMap<Integer, Transaction>();
     
     //transaction ID, and transaction
     HashMap<Integer, Transaction> transactionWaitingList = new HashMap<Integer, Transaction>();
     
     //portID, message packet
     HashMap<Integer, MessagePacket> userList = new HashMap<Integer, MessagePacket>();
     
     
     public Ledger(){
     }
     
     public void confirmTransaction(int transID){
         transactionList.put(transID, transactionWaitingList.get(transID));
         transactionWaitingList.remove(transID);
     }
     
     public void addTransaction(int transID, Transaction trans){
         transactionWaitingList.put(transID, trans);
     }
     
     public void addUser(MessagePacket packet){
         userList.put(packet.portID, packet);
     }
}
