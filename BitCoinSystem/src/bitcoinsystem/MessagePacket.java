/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author Marmota
 */
public class MessagePacket implements Serializable{
    
    int messageID = 0;
    int portID = 0;
    int userPrice = 0;
    int transID = 0;
    int purchaseValue = 0;
    Ledger ledger = null;
    Transaction trans = null;
    PublicKey publicKey= null;
    byte [] signature = null;
    
    //user info message
    public MessagePacket(int mid, int uid, int p, PublicKey pk){
        this.messageID = mid;
        this.portID = uid;
        this.userPrice = p;
        this.publicKey = pk;
    }
    
    //Database message
    public MessagePacket(int mid, Ledger led){
        this.messageID = mid;
        this.ledger = led;
    }
    
    //Transaction Start message
    public MessagePacket(int mid, Transaction t, int tid){
        this.messageID = mid;
        this.trans = t;
        this.transID = tid;
    }
    
    //Purchase Order message
    public MessagePacket(int mid, int pv, int pid){
        this.messageID = mid;
        this.purchaseValue = pv;
        this.portID = pid;
    }
    
    //Generic 
    public MessagePacket(int mid){
        this.messageID = mid;
    }
    
    
}
