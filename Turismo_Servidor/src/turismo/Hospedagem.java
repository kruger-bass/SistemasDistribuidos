/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turismo;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents a lodging option.
 * @author Marmota
 */
public class Hospedagem implements Serializable{
    String cidade;
    String hotel;
    String entrada;
    String diaEntrada;
    String mesEntrada;
    String anoEntrada;
    String saida;
    String diaSaida;
    String mesSaida;
    String anoSaida;
    int quarto;
    int preco;
    
    long expiration;
    
    public Hospedagem(String c, String h, String de, String me, String ae, String ds, String ms, String as, int q, int p){
        
        cidade = c;
        hotel = h;
        diaEntrada = de;
        mesEntrada = me;
        anoEntrada = ae;
        diaSaida = ds;
        mesSaida = ms;
        anoSaida = as;
        quarto = q;
        entrada = de + me + ae;
        saida = ds + ms + as;
        preco = p;
    }
    
    public Hospedagem(String c, String h, String de, String me, String ae, String ds, String ms, String as, int q, long t, int p){
        
        cidade = c;
        hotel = h;
        diaEntrada = de;
        mesEntrada = me;
        anoEntrada = ae;
        diaSaida = ds;
        mesSaida = ms;
        anoSaida = as;
        quarto = q;
        entrada = de + me + ae;
        saida = ds + ms + as;
        expiration = t;
        preco = p;
    }
}
