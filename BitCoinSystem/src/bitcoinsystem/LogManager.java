/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Marmota
 */
public class LogManager {
    
    public void saveLog(Log l){
        Object obj = new Object();
        FileOutputStream fos;
        ObjectOutputStream oos;
        
        try{//cria o file output e object output para salvar o jogo em um arquivo externo
            fos = new FileOutputStream("sav1.marmota");
            oos = new ObjectOutputStream(fos);
            obj = (Object)l;
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            fos.close();
            System.out.println("Seu jogo foi salvo com sucesso!");
        }
        catch(Exception e){
            System.out.println("WARNING! Falha ao salvar arquivo" + e);
        }
    }
    
    public void loadLog(){
        
        Log loadedLog = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        try{// cria os inputs para lerem os arquivos externos criados pelos outputs
                fis = new FileInputStream("sav1.marmota");
                ois = new ObjectInputStream(fis);
                loadedLog = (Log)ois.readObject();
                ois.close();
                fis.close();
                System.out.println("Seu jogo foi carregado com sucesso!");
        }
        catch(Exception e){
                System.out.println("WARNING! Falha ao carregar arquivo");
        }
    }
}
