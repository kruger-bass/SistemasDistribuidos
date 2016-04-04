/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Marmota
 */
public class MessagePacket implements Serializable{
    
    int messageID;
    int userID;
    
    public MessagePacket(int mid, int uid){
        this.messageID = mid;
        this.userID = uid;
    }
    
    public MessagePacket(){}
    
    
}
