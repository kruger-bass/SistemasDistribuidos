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
 * Implementação do Banco de dados local
 * @author Marmota
 */
public class Ledger implements Serializable{
    
    
    //transaction ID, and packet with all info on transaction
     HashMap<Integer, MessagePacket> transactionList = new HashMap<Integer, MessagePacket>();
     
     //transaction ID, and pacote with all info on transaction
     HashMap<Integer, MessagePacket> transactionWaitingList = new HashMap<Integer, MessagePacket>();
     
     //transaction ID, and pacote with all info on transaction
     HashMap<Integer, MessagePacket> transactionAbortedList = new HashMap<Integer, MessagePacket>();
     
     //portID, message packet
     HashMap<Integer, MessagePacket> userList = new HashMap<Integer, MessagePacket>();
     
     /**
      * Construtor
      */
     public Ledger(){
     }
     
     /**
      * Valida a transação, usada pelo mineirador de bitcoins.
      * @param data Vetor de bytes que representa a transação
      * @param signature Assinatura da transação
      * @param pk Chave pública de quem pagou em bitcoins
      * @param transID 
      */
     public boolean confirmTransaction(byte[] data, byte[] signature, PublicKey pk, int transID){
         boolean verified = GenSig.VerifySignature(data, signature, pk);
         if(verified){
            //transactionList.put(transID, transactionWaitingList.get(transID));
            //transactionWaitingList.remove(transID);
         } else{
             System.out.println("Error: ConfirmTransaction");
         }
         return verified;
     }
     
     /**
      * Método que tira uma mensagem da lista de transações a confirmar e coloca na de transações confirmadas
      * @param transID 
      */
     public void transactionConfirmed(int transID){
         
         transactionList.put(transID, transactionWaitingList.get(transID));
         transactionWaitingList.remove(transID);
     }
     
     /**
      * Método que tira uma mensagem da lista de transações a confirmar e coloca na de transações abortadas
      * @param transID 
      */
     public void transactionAborted(int transID){
         
         transactionAbortedList.put(transID, transactionWaitingList.get(transID));
         transactionWaitingList.remove(transID);
     }
     
     /**
      * Método que inclui transação na lista a ser validada
      * @param transID
      * @param packet Guardamos o pacote inteiro
      */
     public void addTransaction(int transID, MessagePacket packet){
         transactionWaitingList.put(transID, packet);
     }
     
     /**
      * Método que inclui usuário no BD
      * @param packet 
      */
     public void addUser(MessagePacket packet){
         userList.put(packet.portID, packet);
     }
}
