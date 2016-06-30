/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public class Log implements Serializable{
    
    int port; // nossa identificação
    int price; // preço do produto
    int wallet; // qt de bitcoins
    int transactionCounter; // numero de transações abertas
    KeyPair keys; // chaves pública e privada
    Calendar lastSeenON; // instant of last log
    HashMap<Integer, MessagePacket> transactionWaitingList;
    
    public Log(int port, int price, int wallet, int transactionCounter, KeyPair keypair, HashMap<Integer, MessagePacket> transactionWaitingList)
    {
        this.port = port;
        this.price = price;
        this.wallet = wallet;
        this.transactionCounter = transactionCounter;
        this.keys = keypair;
        this.lastSeenON = Calendar.getInstance();
        this.transactionWaitingList = transactionWaitingList;
    }
    
}
