/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.*;


/**
 *
 * @author Marmota
 */
public class Transaction implements Serializable{
    
    int senderPort;
    int receiverPort; 
    int value;
    long timestamp;
    
    public Transaction(){}
    
    public Transaction(int sp, int rp, int v, long t){
        this.senderPort = sp;
        this.receiverPort = rp;
        this.value = v;
        this.timestamp = t;
    }
}
