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
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 *
 * @author kruger
 */

public class BitCoinSystem {
    
    public static final int HELLO = 101;
    public static final int WELCOME = 102;
    int port;
    int multiport = 6789;
    BitcoinGUI gui;
    MessagePacket outPacket, inPacket;
    Scanner scan = new Scanner(System.in);
    
    ByteArrayOutputStream byteArrayOS;
    ObjectOutputStream objectOS;
    ByteArrayInputStream byteArrayIS;
    ObjectInputStream objectIS;
    
    public BitCoinSystem(){
        
        System.out.println("Digite a porta unicast que deseja usar: ");
        port = scan.nextInt();
        
        
        
        gui = new BitcoinGUI(this);
        announceEntry();
    }
    
    public static void main(String[] args) {
        BitCoinSystem bitcoin = new BitCoinSystem();
    }
    
    
    //incompleto
    public void announceEntry(){
        
        MulticastSocket s = null;
        outPacket = new MessagePacket(HELLO, port);
        inPacket = new MessagePacket();
		try {
			InetAddress group = InetAddress.getByName("224.0.0.10");
			s = new MulticastSocket(multiport);
			s.joinGroup(group);
                        
                        
 			byte [] m = getOutputStream(outPacket);
                     
                        
			DatagramPacket messageOut = new DatagramPacket(m, m.length, group, multiport);
			s.send(messageOut);	
                        
			byte[] buffer = new byte[1000];
                        
                        for(int i = 0;i<4;i++){
                            DatagramPacket getPacket = new DatagramPacket(buffer, buffer.length);
                            s.receive(getPacket);

                            ByteBuffer wrapped = ByteBuffer.wrap(getPacket.getData());
                            
                            inPacket = getInputStream(wrapped.array());
                            
                            System.out.println("Received: " + inPacket.messageID + ": do usuário da porta " + inPacket.portID);
                            
                            //adiciona no ledger
                            
                            if(inPacket.messageID == WELCOME){
                                //adiciona info no ledger
                                break;
                            }
                        }
                        
                            System.out.println("Há pelo menos 4 usuários no sistema");
                        
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}finally {if(s != null) s.close();}
        
    }
    
    
    //método para mandar bitcoins
    public void sendBitcoin(int port, int value){
        
        // Abre o socket para esta transação
        Socket s = null;
        try
        {
            s = new Socket("127.0.0.1", port);
        }
        catch(UnknownHostException e){System.out.println("Socket:"+e.getMessage());}
        catch (IOException e){System.out.println("readline:"+e.getMessage());}
        finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
        // Socket fechado
        
    }
    
    
    //método para minerar bitcoins
    public void validateTransaction(int transID){
        
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
    public void SendUnicast (Socket CommSocket, String payload) {
		// arguments supply message and hostname
		try{
                            DataOutputStream out =new DataOutputStream( CommSocket.getOutputStream());
                            out.writeUTF(payload);
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}
     }
    
    public String ReceiveUnicast (Socket CommSocket)
    {
        String data = null;
    		try{
                            DataInputStream in = new DataInputStream( CommSocket.getInputStream());
                            data = in.readUTF();	    // read a line of data from the stream
                            System.out.println("Received: "+ data) ; 
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}
                return data;
    }
}
