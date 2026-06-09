package ance;

import java.io.File;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {

        // ── Chemins ─────────────────────────────────────────
        String keystorePath     = "C:/Users/user/Desktop/stage-ance/keystore.p12";
        String keystorePassword = "ance2026";
        String inputFilePath    = "C:/Users/user/Desktop/stage-ance/document_test.xml";
        String outputFilePath   = "C:/Users/user/Desktop/stage-ance/document_signe_java.xml";

        try {
            // 1. Créer le fichier de test si absent
            File testFile = new File(inputFilePath);
            if (!testFile.exists()) {
                try (FileWriter writer = new FileWriter(testFile)) {
                    writer.write("Document de test ANCE — crypto-agilite XAdES.");
                }
                System.out.println("Fichier de test créé : " + inputFilePath);
            }

            // 2. Vérifier le keystore
            if (!new File(keystorePath).exists()) {
                System.err.println(" Keystore introuvable : " + keystorePath);
                System.err.println("→ Génère-le d'abord avec la commande OpenSSL ci-dessous.");
                return;
            }

            // 3. Instancier le moteur
            CryptoAgilityEngine engine = new CryptoAgilityEngine(keystorePath, keystorePassword);

            // 4. Choisir la politique (crypto-agilité = on peut changer cette ligne)
            SignaturePolicy policy = SignaturePolicy.RSA_SHA256();
            // SignaturePolicy policy = SignaturePolicy.ECDSA_SHA256();
            // SignaturePolicy policy = SignaturePolicy.RSA_SHA512();
            // SignaturePolicy policy = SignaturePolicy.ECDSA_SHA512();

            // 5. Signer
            engine.sign(inputFilePath, outputFilePath, policy);

        } catch (Exception e) {
            System.err.println(" Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}