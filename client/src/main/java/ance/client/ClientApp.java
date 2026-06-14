package ance.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import ance.common.CryptoAlgorithm;
import ance.common.SignaturePolicy;
import ance.common.SignatureResponse;

/**
 * Application cliente ANCE - Moteur de Crypto-Agilite
 */
public class ClientApp {

    public static void main(String[] args) {
        try {
            // Configuration
            String serverUrl = "http://localhost:9090/api/";
            String documentPath = "document.xml";
            String outputPath = "document-signed.xml";

            // Lire le document
            byte[] documentBytes = Files.readAllBytes(Paths.get(documentPath));
            System.out.println("📄 Document lu : " + documentPath + " (" + documentBytes.length + " bytes)");

            // === CHOIX DE L'ALGORITHME (CRYPTO-AGILITE) ===
            // Change cette ligne pour tester differents algorithmes :
            
            // CLASSIQUES
            // CryptoAlgorithm chosenAlgorithm = CryptoAlgorithm.RSA_SHA256_2048;
            // CryptoAlgorithm chosenAlgorithm = CryptoAlgorithm.RSA_SHA512_4096;
            // CryptoAlgorithm chosenAlgorithm = CryptoAlgorithm.ECDSA_SHA256_P256;
            
            // POST-QUANTIQUES
            CryptoAlgorithm chosenAlgorithm = CryptoAlgorithm.RSA_SHA256_2048;
            // CryptoAlgorithm chosenAlgorithm = CryptoAlgorithm.FALCON_512;

            SignaturePolicy policy = SignaturePolicy.baselineB(chosenAlgorithm);
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🔧 ALGORITHME CHOISI : " + chosenAlgorithm);
            System.out.println("   Famille : " + (chosenAlgorithm.isPqc() ? "POST-QUANTIQUE" : "CLASSIQUE"));
            System.out.println("   Politique : " + policy);
            System.out.println("=".repeat(50));

            // Creer le client et signer
            RemoteSigner signer = new RemoteSigner(serverUrl);
            
            System.out.println("\n📡 Connexion au serveur ANCE...");
            String health = signer.health();
            System.out.println("   Sante serveur : " + health);

            System.out.println("\n📡 Envoi au serveur ANCE...");
            SignatureResponse response = signer.sign(documentBytes, "document.xml", policy);

            // Traiter la reponse
            if (response.isSuccess()) {
                Files.write(Paths.get(outputPath), 
                    Base64.getDecoder().decode(response.getSignedDocumentBase64()));
                
                System.out.println("\n" + "✅".repeat(25));
                System.out.println("SIGNATURE REUSSIE !");
                System.out.println("   Provider : " + response.getProviderUsed());
                System.out.println("   Algorithme : " + response.getSignatureAlgorithm());
                System.out.println("   Horodatage : " + response.getSigningTime());
                System.out.println("   Fichier : " + outputPath);
                System.out.println("✅".repeat(25));
            } else {
                System.err.println("\n❌ ECHEC : " + response.getErrorMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur client : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
