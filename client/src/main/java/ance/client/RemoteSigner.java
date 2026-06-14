package ance.client;

import ance.common.SignaturePolicy;
import ance.common.SignatureRequest;
import ance.common.SignatureResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;

/**
 * Client HTTP pour communiquer avec le serveur ANCE
 */
public class RemoteSigner {
    
    private final String serverUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public RemoteSigner(String serverUrl) {
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
        this.httpClient = new OkHttpClient();
        this.mapper = new ObjectMapper();
    }

    /**
     * Signer un document via le serveur ANCE
     */
    public SignatureResponse sign(byte[] document, String fileName, SignaturePolicy policy) throws IOException {
        
        String base64Doc = Base64.getEncoder().encodeToString(document);
        
        SignatureRequest request = new SignatureRequest();
        request.setDocumentBase64(base64Doc);
        request.setFileName(fileName);
        request.setPolicy(policy);
        
        String json = mapper.writeValueAsString(request);
        RequestBody body = RequestBody.create(json, JSON);
        
        Request httpRequest = new Request.Builder()
            .url(serverUrl + "crypto-api/sign")
            .post(body)
            .build();
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur serveur : " + response.code());
            }
            return mapper.readValue(response.body().string(), SignatureResponse.class);
        }
    }

    /**
     * Recuperer les algorithmes disponibles
     */
    public String[] getAlgorithms() throws IOException {
        Request request = new Request.Builder()
            .url(serverUrl + "crypto-api/algorithms")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), String[].class);
        }
    }

    /**
     * Verifier la sante du serveur
     */
    public String health() throws IOException {
        Request request = new Request.Builder()
            .url(serverUrl + "crypto-api/health")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
