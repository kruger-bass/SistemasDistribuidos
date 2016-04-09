/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

/**
 * Não utilizado neste projeto. A interface é atualizada diretamente pelos listeners.
 * @author Marmota
 */
public class InterfaceUpdater extends Thread{
    
    BitCoinSystem mainClass;
    BitcoinGUI gui;
    
    /**
     * Construtor do atualizador de interface
     * @param bcs
     * @param bcg 
     */
    public InterfaceUpdater(BitCoinSystem bcs, BitcoinGUI bcg){
        this.mainClass = bcs;
        this.gui = bcg;
    }
    
    /**
     * Método run da thread que atualiza a interface
     */
    public void run(){
        gui.setBitcoinAmountLabel(mainClass.wallet);
    }
}
