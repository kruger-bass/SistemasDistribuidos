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
import java.security.*;


/**
 *
 * @author kruger
 * @author Marmota
 */

public class BitCoinSystem {
    
    //Códigos das mensagens
    public static final int HELLO = 101;
    public static final int WELCOME = 102;
    public static final int VALIDATE = 103;
    public static final int REQUESTTRANSACTION = 104;
    public static final int TRANSACTION = 105;
    public static final int CONFIRMTRANSACTION = 106;
    public static final int REWARDPORT = 6789;
    public static final int REWARDVALUE = 1;
    public static final int EXTRAREWARD = 1;
    
    // variáveis de rede
    int port;
    int multiport = 6789;
    Socket s = null;
    UnicastListener listener = null;
    MulticastListener multiListener = null;
    MulticastSocket m = null;
    InetAddress group;
    
    //variáveis de Mensagem
    MessagePacket outPacket, inPacket;
    ByteArrayOutputStream byteArrayOS;
    ObjectOutputStream objectOS;
    ByteArrayInputStream byteArrayIS;
    ObjectInputStream objectIS;
    
    //Demais variáveis
    Scanner scan = new Scanner(System.in);
    Ledger ledger = new Ledger();
    Transaction reward = new Transaction();
    BitcoinGUI gui;
    long time; 
    int price; //price of your products in bitcoins
    int wallet; //amount of bitcoins
    int transactionCounter; // used to make transaction ID's
    int userCounter = 0;
    KeyPair keyPair = null;
    boolean receivedSignal = false;
    boolean applicationStarted = false;
    
    //Inicializa o usuário de bitcoins
    public BitCoinSystem(){
        // Estado inicial.
        transactionCounter = 0;
        System.out.println("Digite a porta unicast que deseja usar: ");
        port = scan.nextInt();
        System.out.println("Digite qual o preço do seu produto: ");
        price = scan.nextInt();
        keyPair = GenSig.ultra3000KeyPairGenerator();
        wallet = 100;
        
        //Inícialização da comunicação
        try
        {
            listener = new UnicastListener(port, this);
            group = InetAddress.getByName("224.0.0.10");
            m = new MulticastSocket(multiport);
            m.joinGroup(group);
        }catch(UnknownHostException e){System.out.println("Socket:"+e.getMessage());}
        catch (IOException e){System.out.println("readline:"+e.getMessage());}
        multiListener = new MulticastListener(multiport, this);

        // Lançando interface com usuário
        gui = new BitcoinGUI(this);
        // Iniciar sistema!
        announceEntry();
    }
    
    public static void main(String[] args) {
        BitCoinSystem bitcoin = new BitCoinSystem();
    }
    
    
    //Envio da mensagem inicial, com a identificação do usuário
    public void announceEntry(){
        
        outPacket = new MessagePacket(HELLO, port, price, keyPair.getPublic()); // pacote de entrada com a public key e preço das moedas
        sendMulticast(outPacket);
        inPacket = new MessagePacket();
        
        System.out.println("Finalizado anúncio de entrada.");
    }
    
    //método para compra com bitcoins
    public void purchase(int port, int value){
         
        if(value < wallet){
            outPacket = new MessagePacket(REQUESTTRANSACTION, value, this.port);
            sendUnicast(port, outPacket);
        } else{
            System.out.println("Error: Insufficient bitcoin to begin purchase");
        }
    }
    
    
    //método para minerar bitcoins
    public void validateTransaction(int transID){
        System.out.println("dbug validate pre");
        
        if(ledger.transactionWaitingList.containsKey(transID)){//se existe a transação na lista de espera
            
            System.out.println("dbug validate");
            MessagePacket packet = ledger.transactionWaitingList.get(transID);
            PublicKey pubK = ledger.userList.get(packet.trans.senderPort).publicKey;
            ledger.confirmTransaction(getTransactionOutputStream(packet.trans), packet.signature, pubK, transID);
            
            time = System.currentTimeMillis();
            reward = new Transaction(REWARDPORT, port, REWARDVALUE, time);
            outPacket = new MessagePacket(VALIDATE, reward, transID);
            
            sendMulticast(outPacket);
        }
    }
    
    // Envia mensagem no grupo multicast
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
    
    // recebe mensagem do grupo multicast
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
    
    // recebe array de bytes da mensagem
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
    
    // recebe array de bytes da transação
    public byte[] getTransactionOutputStream(Transaction trans){
        
        byte[] data = null;
            try{    
                byteArrayOS = new ByteArrayOutputStream();
                objectOS = new ObjectOutputStream(byteArrayOS);
                
                objectOS.writeObject(trans);
                data = byteArrayOS.toByteArray();
            }catch(Exception e){
                System.out.println("Error in getOutputStream");
            }
        
        return data;
    }
    
    // cria mensagem a partir de array de dados
    public MessagePacket getInputStream(byte[] data){
        MessagePacket packet = null;
            try {
                    System.out.println("debug2");
                    byteArrayIS = new ByteArrayInputStream(data);
                    objectIS = new ObjectInputStream(byteArrayIS);
                    System.out.println("debug1");
                    packet = (MessagePacket) objectIS.readObject();
                    System.out.println("debug0");
                    //System.out.println("Message received = "+packet.messageID);
                } catch(UnknownHostException e){System.out.println("Socket:"+e.getMessage());
                } catch (IOException e){System.out.println("readline:"+e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error in getInputStream");
                }
        
        return packet;
    }
    
    // cria transação a partir do array de dados
    public Transaction getTransactionInputStream(byte[] data){
        
        Transaction trans = null;
            try {
                    byteArrayIS = new ByteArrayInputStream(data);
                    objectIS = new ObjectInputStream(byteArrayIS);
                    
                    trans = (Transaction) objectIS.readObject();
                    //System.out.println("Message received = "+packet.messageID);
                } catch (Exception e) {
                    System.out.println("Error in getInputStream");
                }
        
        return trans;
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
    
}
