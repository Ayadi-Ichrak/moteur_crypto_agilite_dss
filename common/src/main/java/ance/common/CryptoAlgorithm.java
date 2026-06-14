package ance.common;

/**
 * Algorithmes de signature asymetriques supportes par le moteur ANCE
 */
public enum CryptoAlgorithm {
    
    // === CLASSIQUES ===
    RSA_SHA256_2048("RSA", "SHA-256", 2048, "classic"),
    RSA_SHA256_4096("RSA", "SHA-256", 4096, "classic"),
    RSA_SHA512_2048("RSA", "SHA-512", 2048, "classic"),
    RSA_SHA512_4096("RSA", "SHA-512", 4096, "classic"),
    
    ECDSA_SHA256_P256("ECDSA", "SHA-256", 256, "classic"),
    ECDSA_SHA384_P384("ECDSA", "SHA-384", 384, "classic"),
    ECDSA_SHA512_P521("ECDSA", "SHA-512", 521, "classic"),
    
    // === POST-QUANTIQUES ===
    ML_DSA_44("ML-DSA", "SHA-256", 44, "pqc"),
    ML_DSA_65("ML-DSA", "SHA-256", 65, "pqc"),
    ML_DSA_87("ML-DSA", "SHA-256", 87, "pqc"),
    
    FALCON_512("FALCON", "SHA-256", 512, "pqc"),
    FALCON_1024("FALCON", "SHA-256", 1024, "pqc");

    private final String encryption;
    private final String digest;
    private final int keySize;
    private final String family;

    CryptoAlgorithm(String encryption, String digest, int keySize, String family) {
        this.encryption = encryption;
        this.digest = digest;
        this.keySize = keySize;
        this.family = family;
    }

    public String getEncryption() { return encryption; }
    public String getDigest() { return digest; }
    public int getKeySize() { return keySize; }
    public String getFamily() { return family; }
    public boolean isPqc() { return "pqc".equals(family); }
    public boolean isClassic() { return "classic".equals(family); }

    public String getKeyAlias() {
        return name().toLowerCase().replace("_", "");
    }

    @Override
    public String toString() {
        return encryption + "" + keySize + " + " + digest + " [" + family + "]";
    }
}
