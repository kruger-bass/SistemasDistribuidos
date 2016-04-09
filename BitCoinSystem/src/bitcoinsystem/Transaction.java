/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.*;


/**
 * Implementa a classe de transação.
 * @author Marmota
 */
public class Transaction implements Serializable{
    
    int senderPort; // Comprador com bitcoins
    int receiverPort; // Vendedor de um produto
    int value;
    long timestamp;
    
    /**
     * Construtor vazio.
     */
    public Transaction(){}
    
    /**
     * Construtor completo
     * @param sp Porta da pessoa que quer pagar com bitcoins - seu "ID"
     * @param rp Porta da pessoa que vai receber os bitcoins
     * @param v Quantidade de bitcoins relacionados com a transação
     * @param t Timestamp
     */
    public Transaction(int sp, int rp, int v, long t){
        this.senderPort = sp;
        this.receiverPort = rp;
        this.value = v;
        this.timestamp = t;
    }
}
