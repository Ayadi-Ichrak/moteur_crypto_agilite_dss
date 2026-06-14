package ance.common;

/**
 * DTO : Reponse de signature (Serveur -> Client)
 */
public class SignatureResponse {
    
    private boolean success;
    private String signedDocumentBase64;
    private String fileName;
    private String errorMessage;
    private String signatureAlgorithm;
    private String signingTime;
    private String providerUsed;

    public SignatureResponse() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getSignedDocumentBase64() { return signedDocumentBase64; }
    public void setSignedDocumentBase64(String signedDocumentBase64) { this.signedDocumentBase64 = signedDocumentBase64; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }
    
    public String getSigningTime() { return signingTime; }
    public void setSigningTime(String signingTime) { this.signingTime = signingTime; }
    
    public String getProviderUsed() { return providerUsed; }
    public void setProviderUsed(String providerUsed) { this.providerUsed = providerUsed; }

    @Override
    public String toString() {
        return success ? "OK[" + providerUsed + "]" : "ERROR: " + errorMessage;
    }
}
