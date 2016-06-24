/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;

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
    
    
    //Valores da propria carteira
    //Sua identificação
    //data e hora
    //operações realizadas dentro da transação
}
