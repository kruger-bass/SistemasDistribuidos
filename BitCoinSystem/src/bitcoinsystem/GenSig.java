/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinsystem;
import java.io.*;
import java.security.*;
import java.security.spec.*;
/**
 * 
 * @author kruger
 * Implements methods that generates a keypair, sign a transaction and verify a transaction.
 */
public class GenSig implements Serializable{
    
    public GenSig(){}

    // Gera par de chaves usando RSA 1024 bits
    public static KeyPair ultra3000KeyPairGenerator() {
       KeyPair pair = null;
       try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
            keyGen.initialize(1024, random);
            pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
            
        } catch (Exception e){
            System.err.println("Caught exception " + e.toString());
        }
        return pair;
    }
    
    // Assina uma transação
    public static byte[] SignTransaction(PrivateKey priv, byte[] data)
    {
        byte [] realSig = null;
        try{
            Signature sig = Signature.getInstance("SHA256withRSA", "SunRsaSign");
            sig.initSign(priv);
            sig.update(data); // must be a byte[]
            realSig = sig.sign();
        }catch (Exception e){
            System.err.println("Caught exception " + e.toString());
        }
        return realSig;
    }
    
    // Verifica se uma assinatura corresponde a uma transação e um processo.
    public static boolean VerifySignature(byte [] data, byte[] signature, PublicKey key)
    {
        boolean verifies = false;
        try
        {
            Signature sig = Signature.getInstance("SHA256withRSA", "SunRsaSign");
            sig.initVerify(key);
            sig.update(data);
            verifies = sig.verify(signature);
        } catch(Exception e){
            System.err.println("Caught exception " + e.toString());
        }
        return verifies;
    }
}
