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
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

/**
 * Listener do grupo Multicast
 * @author Marmota
 */
public class MulticastListener extends Thread{
    
    int port;
    BitCoinSystem mainClass;
    MulticastSocket multiSocket;
    MessagePacket inPacket, outPacket;
    InetAddress group;
    
    /**
     * Construtor do listener Multicast
     * @param port Porta na qual ele deve ouvir
     * @param bcs instância do sistema de bitcoins
     */
	public MulticastListener (int port, BitCoinSystem bcs) {
            
		try{
                     this.port = port;
                     mainClass = bcs;
                     group = InetAddress.getByName("224.0.0.10");
			multiSocket = new MulticastSocket(port);
                     multiSocket.joinGroup(group);
			this.start();
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
        
        /**
         * Método run da thread Multicast Listener
         */
       public void run(){
       
           try{
			while(true) { // Fique tentando ouvir
				byte[] buffer = new byte[1000];
                                //System.out.println("debug, initiate multicast listen");
                                DatagramPacket getPacket = new DatagramPacket(buffer, buffer.length);
                                multiSocket.receive(getPacket);

                                //System.out.println("got something");
                                ByteBuffer wrapped = ByteBuffer.wrap(getPacket.getData());

                                inPacket = mainClass.getInputStream(wrapped.array());
                                //System.out.println("got this:" + inPacket.messageID);
                                
                                // Envia a mensagem recebida para o método correto
                                if(inPacket.messageID == mainClass.HELLO){

                                    helloHandler(inPacket);

                                } else if(inPacket.messageID == mainClass.TRANSACTIONTOCONFIRM){

                                    confirmTransactionHandler(inPacket);

                                } else if(inPacket.messageID == mainClass.VALIDATE){
                                    
                                    validateHandler(inPacket);

                                } else if(inPacket.messageID == mainClass.ABORT){
                                    
                                    abortTransactionHandler(inPacket);
                                    
                                }
                            
                        }
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
       }
       
       /**
        * função que trata a mensagem Hello
        * @param message mensagem recebida
        */
       public void helloHandler(MessagePacket message){
           
           if(mainClass.applicationStarted == true){ // Se o sistema já está funcionando
               //System.out.println("toaqui");
                outPacket = new MessagePacket(mainClass.WELCOME, mainClass.ledger);
                mainClass.sendUnicast(message.portID, outPacket);
           } else { // Se o sistema ainda está começando
               //System.out.println("tolah");
               //mainClass.trueReceivedSignal(message);
               mainClass.ledger.addUser(message);
               mainClass.userCounter++;
               
               if(mainClass.userCounter >= 4){
                   Iterator it = mainClass.ledger.userList.entrySet().iterator();
                   outPacket = new MessagePacket(mainClass.WELCOME, mainClass.ledger);
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            System.out.println(pair.getKey() + " = " + pair.getValue());
                            try{
                                if(mainClass.port != (int)pair.getKey()){
                                //System.out.println((int)pair.getKey());
                                mainClass.sendUnicast((int)pair.getKey(), outPacket);
                                    System.out.println("Unicast de boas vindas enviado!");
                                }
                            }catch(ClassCastException e){
                                System.out.println(e);
                            }
                        }
               }
           }
       }
       
       /**
        * Manipula mensagem de transação a confirmar
        * @param message 
        */
       public void confirmTransactionHandler(MessagePacket message){
           
           //System.out.println("transIDID:" + message.transID);
           mainClass.ledger.addTransaction(message.transID, message);
           mainClass.logManager.saveLog();
           
       }
       
       /**
        * Manipula mensagem de transação confirmada
        * @param message 
        */
       public void validateHandler(MessagePacket message){
       
           mainClass.ledger.transactionConfirmed(message.transID);
           
           //MessagePacket pack = mainClass.ledger.transactionList.get(message.transID);
           //System.out.println(mainClass.ledger.transactionList.get(message.transID).messageID);
           Transaction aux = mainClass.ledger.transactionList.get(message.transID).trans;
           System.out.println(aux.receiverPort);
           System.out.println(aux.senderPort);
           if(aux.receiverPort == mainClass.port){ // Sou o cara que recebe bitcoins! O Comprador!
               mainClass.wallet += aux.value;
               mainClass.gui.setBitcoinAmountLabel(mainClass.wallet);
           } else if(aux.senderPort == mainClass.port){ // Sou o cara que entrega bitcoins! O Vendedor!
               mainClass.wallet -= aux.value; 
               mainClass.wallet -= mainClass.EXTRAREWARD;
               mainClass.gui.setBitcoinAmountLabel(mainClass.wallet);
           } 
           if(message.trans.receiverPort == mainClass.port){ // Sou o minerador e quero minha recompensa!
               mainClass.wallet += message.trans.value;
               mainClass.wallet += mainClass.EXTRAREWARD;
               mainClass.gui.setBitcoinAmountLabel(mainClass.wallet);
           }
           mainClass.logManager.saveLog();
       }
       
       /**
        * Manipula mensagem de abortar transação
        * @param message 
        */
       public void abortTransactionHandler(MessagePacket message){
           
           //System.out.println("transIDID:" + message.transID);
           mainClass.ledger.transactionAborted(message.transID);
           mainClass.logManager.saveLog();
       }
}