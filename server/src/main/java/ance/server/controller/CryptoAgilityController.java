package ance.server.controller;

import ance.common.SignatureRequest;
import ance.common.SignatureResponse;
import ance.server.service.CryptoAgilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * API REST du moteur de crypto-agilite
 */
@RestController
@RequestMapping("/crypto-api")
public class CryptoAgilityController {

    @Autowired
    private CryptoAgilityService cryptoAgilityService;

    @PostMapping("/sign")
    public ResponseEntity<SignatureResponse> signDocument(@RequestBody SignatureRequest request) {
        SignatureResponse response = cryptoAgilityService.sign(request);
        return response.isSuccess() 
            ? ResponseEntity.ok(response) 
            : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/algorithms")
    public List<String> getAlgorithms() {
        return Arrays.asList(
            "RSA_SHA256_2048", "RSA_SHA256_4096", "RSA_SHA512_2048",
            "ECDSA_SHA256_P256", "ECDSA_SHA384_P384", "ECDSA_SHA512_P521",
            "ML_DSA_44", "ML_DSA_65", "ML_DSA_87",
            "FALCON_512", "FALCON_1024"
        );
    }

    @GetMapping("/health")
    public String health() {
        return "OK - Moteur ANCE Crypto-Agilite actif";
    }
}
