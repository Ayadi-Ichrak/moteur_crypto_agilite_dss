package ance;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;

/**
 * Politique de crypto-agilité — ANCE
 * Permet de changer dynamiquement l'algorithme de signature
 */
public class SignaturePolicy {

    // Algorithme de signature : RSA, ECDSA, (PQC futur)
    private EncryptionAlgorithm encryptionAlgorithm;

    // Algorithme de hachage : SHA256, SHA512
    private DigestAlgorithm digestAlgorithm;

    // Niveau XAdES : BASELINE_B, BASELINE_T, etc.
    private SignatureLevel signatureLevel;

    // Packaging : ENVELOPING, ENVELOPED, DETACHED
    private SignaturePackaging signaturePackaging;

    // ── Politiques prédéfinies ──────────────────────────────

    public static SignaturePolicy RSA_SHA256() {
        return new SignaturePolicy(
            EncryptionAlgorithm.RSA,
            DigestAlgorithm.SHA256,
            SignatureLevel.XAdES_BASELINE_B,
            SignaturePackaging.ENVELOPING
        );
    }

    public static SignaturePolicy ECDSA_SHA256() {
        return new SignaturePolicy(
            EncryptionAlgorithm.ECDSA,
            DigestAlgorithm.SHA256,
            SignatureLevel.XAdES_BASELINE_B,
            SignaturePackaging.ENVELOPING
        );
    }

    public static SignaturePolicy RSA_SHA512() {
        return new SignaturePolicy(
            EncryptionAlgorithm.RSA,
            DigestAlgorithm.SHA512,
            SignatureLevel.XAdES_BASELINE_B,
            SignaturePackaging.ENVELOPING
        );
    }

    public static SignaturePolicy ECDSA_SHA512() {
        return new SignaturePolicy(
            EncryptionAlgorithm.ECDSA,
            DigestAlgorithm.SHA512,
            SignatureLevel.XAdES_BASELINE_B,
            SignaturePackaging.ENVELOPING
        );
    }

    // ── Constructeur ────────────────────────────────────────

    public SignaturePolicy(EncryptionAlgorithm encryptionAlgorithm,
                           DigestAlgorithm digestAlgorithm,
                           SignatureLevel signatureLevel,
                           SignaturePackaging signaturePackaging) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.digestAlgorithm = digestAlgorithm;
        this.signatureLevel = signatureLevel;
        this.signaturePackaging = signaturePackaging;
    }

    // ── Getters ─────────────────────────────────────────────

    public EncryptionAlgorithm getEncryptionAlgorithm() { return encryptionAlgorithm; }
    public DigestAlgorithm getDigestAlgorithm() { return digestAlgorithm; }
    public SignatureLevel getSignatureLevel() { return signatureLevel; }
    public SignaturePackaging getSignaturePackaging() { return signaturePackaging; }

    @Override
    public String toString() {
        return encryptionAlgorithm + " + " + digestAlgorithm + 
               " | " + signatureLevel + " | " + signaturePackaging;
    }
}
