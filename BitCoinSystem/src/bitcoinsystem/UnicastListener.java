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
 * Classe que implementa um listener TCP
 * @author Marmota
 */
public class UnicastListener extends Thread{
    
    int port;
    BitCoinSystem mainClass;
    ServerSocket serverSocket;
    
    /**
    * Construtor do listener
    * Recebe a porta que deve abrir
    * E uma referência à classe principal
    */
	public UnicastListener (int port, BitCoinSystem bcs) {
            
		try{
                     this.port = port;
                     mainClass = bcs;
			serverSocket = new ServerSocket(port);
			this.start();
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
        
       /**
        * Método 'run' para a thread listener.
        * Lança uma nova thread para tratar cada conexão recebida.
        */
        public void run(){ 
       
           try{
			while(true) {
				Socket clientSocket = serverSocket.accept();
				Connection c = new Connection(clientSocket, mainClass);
			}
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
       }
}

/**
 * Classe para tratar cada conexão TCP
 * @author kruger
 */
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
       
       /**
        * 
        * @param aClientSocket Socket para comunicação com o cliente
        * @param bcs Sistema de bitcoin do usuário
        */
	public Connection (Socket aClientSocket, BitCoinSystem bcs) {
		try {
                     mainClass = bcs;
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
        
        /**
         * Método 'run' para a thread Connection.
         * Chama uma função específica para cada tipo de mensagem recebida.
         */
	public void run(){
		try {			                 
                     //Recebe a mensagem e decide o que fazer com ela
                     //Se for mensagem de welcome, substitui o ledger da mainclass
                     //Se for mensagem de purchase, verifica disponibilidade e envia um transaction
                     //Se for mensagem de Transaction Start, assina a mensagem e transmite multicast.
                     byte[] data = new byte[10000];
                     in.read(data);     
                     
                     inPacket = mainClass.getInputStream(data);
                     //System.out.println(inPacket.messageID);
                     
                     if(inPacket.messageID == mainClass.REQUESTTRANSACTION){
                         
                         requestTransactionHandler(inPacket);
                         
                     } else if(inPacket.messageID == mainClass.TRANSACTION){
                         
                         transactionHandler(inPacket);
                         
                     } else if(inPacket.messageID == mainClass.WELCOME){
                         
                         welcomeHandler(inPacket);
                         
                     } else if(inPacket.messageID == mainClass.VALIDATE){
                         
                         mainClass.sendMulticast(inPacket);
                        
                     } else if (inPacket.messageID == mainClass.ABORT){
                         
                         mainClass.sendMulticast(inPacket);
                         
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
         * Método que cria transação
         * @param message 
         */
       public void requestTransactionHandler(MessagePacket message){
           
           transaction = new Transaction(message.portID, mainClass.port, message.purchaseValue, System.currentTimeMillis());
           outPacket = new MessagePacket(mainClass.TRANSACTION, transaction, ((mainClass.port*100) + mainClass.transactionCounter));
           //System.out.println("TransID: " + outPacket.transID);
           mainClass.transactionCounter++;
           mainClass.sendUnicast(message.portID, outPacket);
           //System.out.println(message.portID);
       }
       
       /**
        * Método que assina transação
        * @param message mensagem recebida com a transação.
        */
       public void transactionHandler(MessagePacket message){
           
           byte[] signature = GenSig.SignTransaction(mainClass.keyPair.getPrivate(), mainClass.getTransactionOutputStream(message.trans));
           //System.out.println(mainClass.keyPair.getPrivate());
           //System.out.println(mainClass.getTransactionOutputStream(message.trans));
           outPacket = new MessagePacket(mainClass.TRANSACTIONTOCONFIRM, message.trans, message.transID, signature);
           //System.out.println("transID" + outPacket.transID);
           mainClass.sendMulticast(outPacket);
           //System.out.println("th");
       }
       
       /**
        * método que recebe o banco de dados distribuído.
        * @param message mensagem com o estado atual do sistema
        */
       public void welcomeHandler(MessagePacket message){
           
           mainClass.ledger.transactionAbortedList = message.ledger.transactionAbortedList;
           mainClass.ledger.transactionList = message.ledger.transactionList;
           mainClass.ledger.userList = message.ledger.userList;
           if (mainClass.ledger.transactionWaitingList == null)
               mainClass.ledger.transactionWaitingList = message.ledger.transactionWaitingList;
           
           System.out.println("recebido boas-vindas");
       }
}
