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
public class Transaction implements Serializable{
    
    int senderPort;
    int receiverPort; //receiver port 6789 é recompensa de mineração
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
