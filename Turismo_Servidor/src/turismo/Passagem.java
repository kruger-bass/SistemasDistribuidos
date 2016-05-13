/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents an Airfare option
 * @author Marmota
 */
public class Passagem implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;
    String origem;
    String destino;
    String diaIda;
    String mesIda;
    String anoIda;
    String diaVolta;
    String mesVolta;
    String anoVolta;
    int qtd;
    int preco;
    
    long expiration;

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
    public Passagem(String o, String d, String di, String mi, String ai, int q, int p, long t){
        
        origem = o;
        destino = d;
        diaIda = di;
        mesIda = mi;
        anoIda = ai;
        qtd = q;
        preco = p;
        expiration = t;
}
    public Passagem(String o, String d, String di, String mi, String ai, String dv, String mv, String av, int q, int p){
        
        origem = o;
        destino = d;
        diaIda = di;
        mesIda = mi;
        anoIda = ai;
        diaVolta = dv;
        mesVolta = mv;
        anoVolta = av;
        qtd = q;
        preco = p;
}
    
}
