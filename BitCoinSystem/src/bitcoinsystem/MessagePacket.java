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
    
    int messageID;
    int portID;
    int publicKey;
    int price;
    
    public MessagePacket(int mid, int uid){
        this.messageID = mid;
        this.portID = uid;
    }
    
    public MessagePacket(){}
    
    
}
