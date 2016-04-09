/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Classe genérica para mensagens
 * @author Marmota
 */
public class MessagePacket implements Serializable{
    
    // Todas as variáveis utilizadas pelas nossas mensagens
    int messageID = 0;
    int portID = 0;
    int userPrice = 0;
    int transID = 0;
    int purchaseValue = 0;
    Ledger ledger = null;
    Transaction trans = null;
    PublicKey publicKey= null;
    byte [] signature = null;
    
    /**
     * Construtor para mensagem com infos do usuário
     * @param mid Tipo de mensagem enviada
     * @param uid Identificação do usuário
     * @param p Preço do produto
     * @param pk Chave pública
     */
    public MessagePacket(int mid, int uid, int p, PublicKey pk){
        this.messageID = mid; //Hello
        this.portID = uid;
        this.userPrice = p;
        this.publicKey = pk;
    }
    
    /**
     * Construtor da Database message
     * @param mid
     * @param led 
     */
    public MessagePacket(int mid, Ledger led){
        this.messageID = mid; //Welcome
        this.ledger = led;
    }
    
    /**
     * Construtor da Transaction Start message 
     * @param mid
     * @param t
     * @param tid 
     */
    public MessagePacket(int mid, Transaction t, int tid){
        this.messageID = mid; //Transaction
        this.trans = t;
        this.transID = tid;
    }
    
    /**
     * Construtor da Purchase Order message
     * @param mid
     * @param pv
     * @param pid 
     */
    public MessagePacket(int mid, int pv, int pid){
        this.messageID = mid; //RequestTransaction
        this.purchaseValue = pv;
        this.portID = pid;
    }
    
    /**
     * Construtor da mensagem de transação assinada
     * @param mid
     * @param t
     * @param tid
     * @param s 
     */
    public MessagePacket(int mid, Transaction t, int tid, byte[] s){
        this.messageID = mid;
        this.trans = t;
        this.transID = tid;
        this.signature = s;
    }
    
    /**
     * Construtor vazio
     */
    public MessagePacket(){}
}
