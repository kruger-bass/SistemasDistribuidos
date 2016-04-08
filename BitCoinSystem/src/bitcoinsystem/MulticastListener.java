/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * @author Marmota
 */
public class MulticastListener extends Thread{
    
    int port;
    BitCoinSystem mainClass;
    MulticastSocket multiSocket;
    MessagePacket inPacket, outPacket;
    
	public MulticastListener (int port, BitCoinSystem bcs) {
            
		try{
                     this.port = port;
                     mainClass = bcs;
			multiSocket = new MulticastSocket(port);
			this.start();
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
        
       public void run(){
       
           try{
			while(true) {
				byte[] buffer = new byte[1000];
                        
                            DatagramPacket getPacket = new DatagramPacket(buffer, buffer.length);
                            multiSocket.receive(getPacket);
                            ByteBuffer wrapped = ByteBuffer.wrap(getPacket.getData());
                            
                            inPacket = mainClass.getInputStream(wrapped.array());
                            
                            if(inPacket.messageID == mainClass.HELLO){
                                
                                helloHandler(inPacket);
                                
                            } else if(inPacket.messageID == mainClass.CONFIRMTRANSACTION){
                                
                                transactionHandler(inPacket);
                                
                            } else if(inPacket.messageID == mainClass.VALIDATE){
                                
                                validateHandler(inPacket);
                                
                            }
                            
                        }
		} catch(Exception e) {System.out.println("Listen socket:"+e.getMessage());}
       }
       
       public void helloHandler(MessagePacket message){
           
           outPacket = new MessagePacket(mainClass.WELCOME, mainClass.ledger);
           mainClass.sendUnicast(message.portID, outPacket);
           
       }
       
       public void transactionHandler(MessagePacket message){
       
           mainClass.ledger.addTransaction(message.transID, message);
           
       }
       
       public void validateHandler(MessagePacket message){
       
           mainClass.ledger.transactionConfirmed(message.transID);
           
           if(message.trans.senderPort == mainClass.port){
               mainClass.wallet =- message.trans.value;
               mainClass.gui.setBitcoinAmountLabel(mainClass.wallet);
           } else if(message.trans.receiverPort == mainClass.port){
               mainClass.wallet =+ message.trans.value;
               mainClass.gui.setBitcoinAmountLabel(mainClass.wallet);
           }
       }
}
