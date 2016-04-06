/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;

/**
 *
 * @author Marmota
 */
public class MessagePacket implements Serializable{
    
    int messageID = 0;
    int portID = 0;
    int publicKey = 0;
    int price = 0;
    int transID = 0;
    Ledger ledger = null;
    Transaction trans = null;
    
    public MessagePacket(int mid, int uid){
        this.messageID = mid;
        this.portID = uid;
    }
    
    public MessagePacket(int mid, Ledger led){
        this.messageID = mid;
        this.ledger = led;
    }
    
    public MessagePacket(int mid, Transaction t, int tid){
        this.messageID = mid;
        this.trans = t;
        this.transID = tid;
    }
    
    public MessagePacket(){}
    
    
}
