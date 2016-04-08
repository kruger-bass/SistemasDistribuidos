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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marmota
 */
/*
public class UnicastListener extends Thread{
    
       DataInputStream in;
	ServerSocket ss;
       BitCoinSystem mainClass;
        
	public UnicastListener (int port, BitCoinSystem bcs) {
            
        try {            
            
            ss = new ServerSocket(port);
            mainClass = bcs;
            this.start();
            
        } catch (IOException ex) {
            Logger.getLogger(UnicastListener.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
        
	public void run(){
		try {	
			while(true) {
				Socket clientSocket = ss.accept();
                            
			}
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} finally{ 
                    try {
                        clientSocket.close();
                    }catch (IOException e){
                   
                    }
                }
		

	}
}
*/
public class UnicastListener extends Thread{
    
    int port;
    BitCoinSystem mainClass;
    ServerSocket serverSocket;
    
	public UnicastListener (int port, BitCoinSystem bcs) {
            
		try{
                     this.port = port;
                     mainClass = bcs;
			serverSocket = new ServerSocket(port);
			this.start();
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
        
       public void run(){
       
           try{
			while(true) {
				Socket clientSocket = serverSocket.accept();
				Connection c = new Connection(clientSocket, mainClass);
			}
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
       }
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
        
       MessagePacket inPacket;
       MessagePacket outPacket;
       ByteArrayOutputStream byteArrayOS;
       ObjectOutputStream objectOS;
       ByteArrayInputStream byteArrayIS;
       ObjectInputStream objectIS;
       
       BitCoinSystem mainClass;
       Transaction transaction;
       
	public Connection (Socket aClientSocket, BitCoinSystem bcs) {
		try {
                     mainClass = bcs;
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
        
	public void run(){
		try {			                 
                     //Recebe a mensagem e decide o que fazer com ela
                     //Se for mensagem de welcome, substitui o ledger da mainclass
                     //Se for mensagem de purchase, verifica disponibilidade e envia um transaction
                     byte[] data = new byte[1000];
                     in.read(data);     
                     
                     inPacket = mainClass.getInputStream(data);
                     System.out.println(inPacket.messageID);
                     
                     if(inPacket.messageID == mainClass.REQUESTTRANSACTION){
                         
                         requestTransactionHandler(inPacket);
                         
                     } else if(inPacket.messageID == mainClass.TRANSACTION){
                         
                         transactionHandler(inPacket);
                         
                     } else if(inPacket.messageID == mainClass.WELCOME){
                         
                         welcomeHandler(inPacket);
                         
                     }
                     
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} finally{ 
                    try {
                        clientSocket.close();
                    }catch (IOException e){
                    /*close failed*/
                    }
                }
		

	}
        
       /**
        * 
        * 
        * @param message 
        */
       public void requestTransactionHandler(MessagePacket message){
           
           transaction = new Transaction(message.portID, mainClass.port, message.purchaseValue, System.currentTimeMillis());
           outPacket = new MessagePacket(mainClass.TRANSACTION, transaction, ((mainClass.port*100) + mainClass.transactionCounter));
           mainClass.transactionCounter++;
           mainClass.sendUnicast(message.portID, message);
       }
       
       public void transactionHandler(MessagePacket message){
           
           //assinar transaçao
       }
       
       public void welcomeHandler(MessagePacket message){
           
           mainClass.ledger = message.ledger;
       }
}