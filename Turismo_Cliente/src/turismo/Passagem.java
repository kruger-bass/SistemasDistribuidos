/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Marmota
 */
public class Passagem implements Serializable{
    String origem;
    String destino;
    String diaIda;
    String mesIda;
    String anoIda;
    int qtd;
    int preco;

    public Passagem(){}
    
    public Passagem(String o, String d, String di, String mi, String ai, int q, int p){
        
        origem = o;
        destino = d;
        diaIda = di;
        mesIda = mi;
        anoIda = ai;
        qtd = q;
        preco = p;
}
    
    public void setPassagem(String o, String d, String di, String mi, String ai, int q, int p){
        
        origem = o;
        destino = d;
        diaIda = di;
        mesIda = mi;
        anoIda = ai;
        qtd = q;
        preco = p;
    }
}
