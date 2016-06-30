/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.util.HashMap;

/**
 *
 * @author Marmota
 */
public class LogManager {
    
    BitCoinSystem mainClass;
    
    public LogManager(BitCoinSystem btc){
        this.mainClass = btc;
    }
    
    public void saveLog(){
        Object obj = new Object();
        FileOutputStream fos;
        ObjectOutputStream oos;
        Log l = new Log(mainClass.port, mainClass.price, mainClass.wallet, mainClass.transactionCounter, mainClass.keyPair, mainClass.ledger.transactionWaitingList);
        
        try{//cria o file output e object output para salvar o jogo em um arquivo externo
            fos = new FileOutputStream(mainClass.port + ".bit");
            oos = new ObjectOutputStream(fos);
            obj = (Object)l;
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            fos.close();
            System.out.println("Log salvo com sucesso!");
        }
        catch(Exception e){
            System.out.println("WARNING! Falha ao salvar arquivo" + e);
        }
    }
    
    public boolean loadLog(){
        boolean ret = false;
        Log loadedLog = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        try{// cria os inputs para lerem os arquivos externos criados pelos outputs
                fis = new FileInputStream(mainClass.port + ".bit");
                ois = new ObjectInputStream(fis);
                loadedLog = (Log)ois.readObject();
                ois.close();
                fis.close();
                System.out.println("Seu jogo foi carregado com sucesso!");
                mainClass.price = loadedLog.price;
                mainClass.wallet = loadedLog.wallet;
                mainClass.transactionCounter = loadedLog.transactionCounter;
                mainClass.keyPair = loadedLog.keys;
                mainClass.ledger.transactionWaitingList = loadedLog.transactionWaitingList;
                ret = true;
        }
        catch(Exception e){
                System.out.println("WARNING! Falha ao carregar arquivo");
        }
        return ret;
    }
}
