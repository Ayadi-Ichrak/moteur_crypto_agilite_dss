package ance.common;

/**
 * Interface abstraite pour les providers de signature
 */
public interface CryptoProvider {
    
    /**
     * Signe un document selon la politique donnee
     * @param document document en bytes
     * @param policy politique de signature
     * @return document signe en bytes
     * @throws Exception en cas d'erreur
     */
    byte[] sign(byte[] document, SignaturePolicy policy) throws Exception;
    
    /**
     * Verifie une signature
     * @param document document original
     * @param signature signature a verifier
     * @param policy politique utilisee
     * @return true si valide
     * @throws Exception en cas d'erreur
     */
    boolean verify(byte[] document, byte[] signature, SignaturePolicy policy) throws Exception;
}
