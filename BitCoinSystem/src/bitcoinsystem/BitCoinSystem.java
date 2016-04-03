/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 *
 * @author kruger
 */

public class BitCoinSystem {
    
    public static final int HELLO = 101;
    int port;
    int multiport = 6789;
    BitcoinGUI gui;
    MessagePacket packet;
    Scanner scan = new Scanner(System.in);
    
    public BitCoinSystem(){
        
        System.out.println("Digite a porta que deseja usar: ");
        port = scan.nextInt();
        
        
        
        gui = new BitcoinGUI(this);
        announceEntry();
    }
    
    public static void main(String[] args) {
        BitCoinSystem bitcoin = new BitCoinSystem();
    }
    
    
    //método para dar oi quando entra
    public void announceEntry(){
        
        MulticastSocket s = null;
        
		try {
			InetAddress group = InetAddress.getByName("224.0.0.10");
			s = new MulticastSocket(multiport);
			s.joinGroup(group);
                        
 			byte [] m = ByteBuffer.allocate(4).putInt(HELLO).array();
			DatagramPacket messageOut = new DatagramPacket(m, m.length, group, multiport);
			s.send(messageOut);	
                        
			byte[] buffer = new byte[1000];
                     DatagramPacket getPacket = new DatagramPacket(buffer, buffer.length);
                     s.receive(getPacket);

                     ByteBuffer wrapped = ByteBuffer.wrap(getPacket.getData());
                     int received = wrapped.getInt();
                     System.out.println("Received: " + new String(getPacket.getData()) + ": da porta " + getPacket.getPort());
                        		
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}finally {if(s != null) s.close();}
        
    }
    
    
}
