package ance.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

/**
 * Chargeur de configuration JSON
 */
public class ConfigLoader {
    
    public static CryptoConfig loadConfig(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), CryptoConfig.class);
    }

    public static class CryptoConfig {
        public Server server;
        public DefaultPolicy defaultPolicy;
        public Client client;

        public static class Server {
            public String host;
            public int port;
            public String contextPath;
        }

        public static class DefaultPolicy {
            public String algorithm;
            public String signatureLevel;
            public String signaturePackaging;
        }

        public static class Client {
            public Keystore keystore;

            public static class Keystore {
                public String path;
                public String password;
                public String type;
            }
        }
    }
}
