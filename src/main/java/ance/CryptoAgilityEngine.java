package ance;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

/**
 * Moteur de Crypto-Agilité 
 * Signe un document XAdES selon une politique configurable
 */
public class CryptoAgilityEngine {

    private final String keystorePath;
    private final String keystorePassword;

    public CryptoAgilityEngine(String keystorePath, String keystorePassword) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
    }

    /**
     * Signe un document selon la politique fournie
     * @param inputFilePath  fichier à signer
     * @param outputFilePath fichier signé en sortie
     * @param policy         politique de crypto-agilité
     */
    public void sign(String inputFilePath, String outputFilePath, SignaturePolicy policy) throws Exception {

        System.out.println("===========================================");
        System.out.println("  MOTEUR CRYPTO-AGILITE ");
        System.out.println("===========================================");
        System.out.println("Politique : " + policy);
        System.out.println("Fichier   : " + inputFilePath);
        System.out.println("-------------------------------------------");

        // ── Étape 1 : Charger le keystore PKCS#12 ───────────
        System.out.println("[1/4] Chargement du keystore...");
        KeyStore.PasswordProtection password =
            new KeyStore.PasswordProtection(keystorePassword.toCharArray());

        Pkcs12SignatureToken token = new Pkcs12SignatureToken(
            new FileInputStream(keystorePath), password
        );

        DSSPrivateKeyEntry privateKey = token.getKeys().get(0);
        System.out.println("      Certificat : " + 
            privateKey.getCertificate().getSubject().getRFC2253());

        // ── Étape 2 : Configurer les paramètres XAdES ────────
        System.out.println("[2/4] Configuration des paramètres XAdES...");
        XAdESSignatureParameters parameters = new XAdESSignatureParameters();
        parameters.setSignatureLevel(policy.getSignatureLevel());
        parameters.setSignaturePackaging(policy.getSignaturePackaging());
        parameters.setDigestAlgorithm(policy.getDigestAlgorithm());
        parameters.setEncryptionAlgorithm(policy.getEncryptionAlgorithm());
        parameters.setSigningCertificate(privateKey.getCertificate());

        // ── Étape 3 : Charger le document à signer ───────────
        System.out.println("[3/4] Chargement du document...");
        DSSDocument documentToSign = new FileDocument(new File(inputFilePath));

        // ── Étape 4 : Les 3 opérations DSS ───────────────────
        System.out.println("[4/4] Signature en cours...");
        CommonCertificateVerifier verifier = new CommonCertificateVerifier();
        XAdESService service = new XAdESService(verifier);

        // 4a. getDataToSign
        ToBeSigned dataToSign = service.getDataToSign(documentToSign, parameters);

        // 4b. sign avec la clé privée
        SignatureValue signatureValue = token.sign(
            dataToSign,
            parameters.getDigestAlgorithm(),
            privateKey
        );

        // 4c. signDocument — assemblage final
        DSSDocument signedDocument = service.signDocument(
            documentToSign, parameters, signatureValue
        );

        // ── Sauvegarde ────────────────────────────────────────
        signedDocument.save(outputFilePath);
        token.close();

        System.out.println("-------------------------------------------");
        System.out.println(" Signature réussie !");
        System.out.println("   Fichier signé : " + outputFilePath);
        System.out.println("===========================================");
    }
}