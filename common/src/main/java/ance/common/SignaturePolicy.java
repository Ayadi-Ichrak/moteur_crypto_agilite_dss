package ance.common;

/**
 * Politique de signature configurable
 */
public class SignaturePolicy {
    
    private CryptoAlgorithm algorithm;
    private String signatureLevel;
    private String signaturePackaging;
    private boolean useTsa;
    private String tsaUrl;

    public SignaturePolicy() {}

    public SignaturePolicy(CryptoAlgorithm algorithm, String level, String packaging) {
        this.algorithm = algorithm;
        this.signatureLevel = level;
        this.signaturePackaging = packaging;
    }

    public static SignaturePolicy baselineB(CryptoAlgorithm algo) {
        return new SignaturePolicy(algo, "XAdES_BASELINE_B", "ENVELOPED");
    }

    public static SignaturePolicy baselineT(CryptoAlgorithm algo, String tsaUrl) {
        SignaturePolicy p = new SignaturePolicy(algo, "XAdES_BASELINE_T", "ENVELOPED");
        p.useTsa = true;
        p.tsaUrl = tsaUrl;
        return p;
    }

    public CryptoAlgorithm getAlgorithm() { return algorithm; }
    public void setAlgorithm(CryptoAlgorithm algorithm) { this.algorithm = algorithm; }
    
    public String getSignatureLevel() { return signatureLevel; }
    public void setSignatureLevel(String signatureLevel) { this.signatureLevel = signatureLevel; }
    
    public String getSignaturePackaging() { return signaturePackaging; }
    public void setSignaturePackaging(String signaturePackaging) { this.signaturePackaging = signaturePackaging; }
    
    public boolean isUseTsa() { return useTsa; }
    public void setUseTsa(boolean useTsa) { this.useTsa = useTsa; }
    
    public String getTsaUrl() { return tsaUrl; }
    public void setTsaUrl(String tsaUrl) { this.tsaUrl = tsaUrl; }

    @Override
    public String toString() {
        return algorithm + " | " + signatureLevel + " | " + signaturePackaging;
    }
}
