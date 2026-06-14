package ance.server;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

/**
 * Serveur ANCE - Moteur de Crypto-Agilite
 * Port : 9090
 */
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            System.out.println("🔐 BouncyCastle enregistre comme provider de securite");
        }

        SpringApplication.run(ServerApp.class, args);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🚀 SERVEUR ANCE CRYPTO-AGILITE");
        System.out.println("   URL : http://localhost:9090/api");
        System.out.println("   Providers : Classic (RSA/ECDSA) + PQC (ML-DSA/FALCON)");
        System.out.println("=".repeat(50) + "\n");
    }
}
