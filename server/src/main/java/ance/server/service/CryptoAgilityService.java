package ance.server.service;

import ance.common.CryptoAlgorithm;
import ance.common.SignaturePolicy;
import ance.common.SignatureRequest;
import ance.common.SignatureResponse;
import ance.server.providers.ClassicProvider;
import ance.server.providers.PqcProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;

/**
 * Couche 2 - Crypto API (Policy Enforcement)
 * Route vers le bon provider selon l'algorithme
 */
@Service
public class CryptoAgilityService {

    @Autowired
    private ClassicProvider classicProvider;

    @Autowired
    private PqcProvider pqcProvider;

    public SignatureResponse sign(SignatureRequest request) {
        SignatureResponse response = new SignatureResponse();
        
        try {
            SignaturePolicy policy = request.getPolicy();
            CryptoAlgorithm algo = policy.getAlgorithm();
            
            System.out.println("\n" + "-".repeat(40));
            System.out.println("[POLICY ENFORCEMENT] Algorithme : " + algo);
            System.out.println("                     Politique : " + policy);

            byte[] documentBytes = Base64.getDecoder().decode(request.getDocumentBase64());
            
            byte[] signedDocument;
            String providerUsed;
            
            if (algo.isClassic()) {
                System.out.println(" -> Routage vers Provider 1 : Classique");
                signedDocument = classicProvider.sign(documentBytes, policy);
                providerUsed = "classic";
            } else if (algo.isPqc()) {
                System.out.println(" -> Routage vers Provider 2 : Post-Quantique");
                signedDocument = pqcProvider.sign(documentBytes, policy);
                providerUsed = "pqc";
            } else {
                throw new IllegalArgumentException("Famille d'algorithme inconnue");
            }

            response.setSuccess(true);
            response.setSignedDocumentBase64(Base64.getEncoder().encodeToString(signedDocument));
            response.setFileName("signed-" + request.getFileName());
            response.setSignatureAlgorithm(algo.toString());
            response.setSigningTime(Instant.now().toString());
            response.setProviderUsed(providerUsed);

            System.out.println(" ✅ Signature reussie via provider : " + providerUsed);
            System.out.println("-".repeat(40));

        } catch (Exception e) {
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            System.err.println(" ❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
