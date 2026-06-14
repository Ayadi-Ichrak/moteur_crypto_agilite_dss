package ance.common;

/**
 * DTO : Requete de signature (Client -> Serveur)
 */
public class SignatureRequest {
    
    private String documentBase64;
    private String fileName;
    private SignaturePolicy policy;
    private String keyAlias;

    public SignatureRequest() {}

    public String getDocumentBase64() { return documentBase64; }
    public void setDocumentBase64(String documentBase64) { this.documentBase64 = documentBase64; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public SignaturePolicy getPolicy() { return policy; }
    public void setPolicy(SignaturePolicy policy) { this.policy = policy; }
    
    public String getKeyAlias() { return keyAlias; }
    public void setKeyAlias(String keyAlias) { this.keyAlias = keyAlias; }

    @Override
    public String toString() {
        return "SignatureRequest{file='" + fileName + "', policy=" + policy + "}`";
    }
}
