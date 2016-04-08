/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kruger
 * @author Marmota
 */

public class BitCoinSystem {
    
    public static final int HELLO = 101;
    public static final int WELCOME = 102;
    public static final int VALIDATE = 103;
    public static final int REQUESTTRANSACTION = 104;
    public static final int TRANSACTION = 105;
    public static final int CONFIRMTRANSACTION = 106;
    public static final int REWARDPORT = 6789;
    public static final int REWARDVALUE = 1;
    
    int port;
    int multiport = 6789;
    Socket s = null;
    UnicastListener listener = null;
    MulticastSocket m = null;
    InetAddress group;
    
    MessagePacket outPacket, inPacket;
    ByteArrayOutputStream byteArrayOS;
    ObjectOutputStream objectOS;
    ByteArrayInputStream byteArrayIS;
    ObjectInputStream objectIS;
    
    Scanner scan = new Scanner(System.in);
    Ledger ledger = new Ledger();
    Transaction reward = new Transaction();
    BitcoinGUI gui;
    long time; 
    int price; //price of your products in bitcoins
    int wallet; //amount of bitocins
    int transactionCounter; // used to make transaction ID's
    
    
    
    public BitCoinSystem(){
        
        System.out.println("Digite a porta unicast que deseja usar: ");
        port = scan.nextInt();

        
        System.out.println("Digite qual o preço do seu produto: ");
        price = scan.nextInt();
        
        try
        {
            listener = new UnicastListener(port, this);
            group = InetAddress.getByName("224.0.0.10");
            m = new MulticastSocket(multiport);
            m.joinGroup(group);
        }catch(UnknownHostException e){System.out.println("Socket:"+e.getMessage());}
        catch (IOException e){System.out.println("readline:"+e.getMessage());}
        
        gui = new BitcoinGUI(this);
        announceEntry();
    }
    
    public static void main(String[] args) {
        BitCoinSystem bitcoin = new BitCoinSystem();
    }
    
    
    //incompleto
    public void announceEntry(){
        
        outPacket = new MessagePacket(HELLO, port, price);
        sendMulticast(outPacket);
        
        for(int i=0;i<4;i++){
            inPacket = receiveMulticast();
            
            if(inPacket.messageID == HELLO){
                ledger.addUser(inPacket);
                System.out.println("Recebido uma mensagem de Olá");
                if(i == 3){
                    System.out.println("4 usuários no sistema, enviando Boas-Vindas.");
                    outPacket = new MessagePacket(WELCOME, ledger);
                    //envia o WELCOME via unicast
                    
                    Iterator it = ledger.userList.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        try{
                            if(port != (int)pair.getKey()){
                            System.out.println((int)pair.getKey());
                            sendUnicast((int)pair.getKey(), outPacket);
                            }
                        }catch(ClassCastException e){
                            System.out.println(e);
                        }
                    }
                }
            }
            
            if(inPacket.messageID == WELCOME){
                System.out.println("Recebido uma mensagem de Boas-Vindas");
                ledger = inPacket.ledger;
                break;
            }
        }
        System.out.println("Finalizado anúncio de entrada.");
    }
    
    
    //método para compra com bitcoins
    public void purchase(int port, int value){
         
        if(value <= wallet){
            outPacket = new MessagePacket(REQUESTTRANSACTION, value, port);
            sendUnicast(port, outPacket);
        } else{
            System.out.println("Error: Insufficient bitcoin to begin purchase");
        }
    }
    
    
    //método para minerar bitcoins
    public void validateTransaction(int transID){
        
        if(ledger.transactionWaitingList.containsKey(transID)){
        
            //ledger.confirmTransaction(transID);
            // colocar a chave publica do sender
            
            time = System.currentTimeMillis();
            reward = new Transaction(port, REWARDPORT, REWARDVALUE, time);
            outPacket = new MessagePacket(VALIDATE, reward, transID);
            
            sendMulticast(outPacket);
        }
    }
    
    
    public void sendMulticast(MessagePacket message){
    
        try {
 			byte [] m = getOutputStream(message);
			DatagramPacket messageOut = new DatagramPacket(m, m.length, group, multiport);
			this.m.send(messageOut);	
                        
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	 }catch (IOException e){System.out.println("IO: " + e.getMessage());
	 }catch (Exception e){System.out.println("Error in sendMulticast");
        }
    }
    
    public MessagePacket receiveMulticast(){
    
       
        try {
            byte[] buffer = new byte[1000];
                        
            DatagramPacket getPacket = new DatagramPacket(buffer, buffer.length);
            m.receive(getPacket);
            ByteBuffer wrapped = ByteBuffer.wrap(getPacket.getData());
                            
            inPacket = getInputStream(wrapped.array());
                            
        } catch (Exception e) {
            System.out.println("Error in receiveMulticast");
        }
        
        return inPacket;

    }
    
    public byte[] getOutputStream(MessagePacket message){
        
        byte[] data = null;
            try{    
                byteArrayOS = new ByteArrayOutputStream();
                objectOS = new ObjectOutputStream(byteArrayOS);
                
                objectOS.writeObject(message);
                data = byteArrayOS.toByteArray();
            }catch(Exception e){
                System.out.println("Error in getOutputStream");
            }
        
        return data;
    }
    
    public MessagePacket getInputStream(byte[] data){
        
        MessagePacket packet = null;
            try {
                    byteArrayIS = new ByteArrayInputStream(data);
                    objectIS = new ObjectInputStream(byteArrayIS);
                    
                    packet = (MessagePacket) objectIS.readObject();
                    //System.out.println("Message received = "+packet.messageID);
                } catch (Exception e) {
                    System.out.println("Error in getInputStream");
                }
        
        return packet;
    }
    
    // Métodos que mandam mensagens unicast no localHost    
    public void sendUnicast (int port, MessagePacket message) {
		// arguments supply message and hostname
        
		try{
                            s = new Socket("localhost", port);
                            DataOutputStream out = new DataOutputStream(s.getOutputStream());
                            out.write(getOutputStream(message));
		}catch (IOException e){
                    System.out.println("readline:"+e.getMessage());
		}
     }
    // TODO: thread that keeps listening on PORT, them reads and resolves that message.
}
