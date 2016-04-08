/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;

/**
 *
 * @author Marmota
 */
public class InterfaceUpdater extends Thread{
    
    BitCoinSystem mainClass;
    BitcoinGUI gui;
    
    public InterfaceUpdater(BitCoinSystem bcs, BitcoinGUI bcg){
        this.mainClass = bcs;
        this.gui = bcg;
    }
    
    public void run(){
        gui.setBitcoinAmountLabel(mainClass.wallet);
    }
}
