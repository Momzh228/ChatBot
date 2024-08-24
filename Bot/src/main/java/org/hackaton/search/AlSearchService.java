package org.hackaton.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;

public class AlSearchService {

    private static final String API_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    @Value("${client.secret}")
    private String CLIENT_SECRET;
    @Value("${authorization}")
    private String AUTHORIZATION;

    public String getToken() {
        configureSSL();
        return connect();
    }

    private void configureSSL() {
        try {
            // Загрузка сертификата из ресурсов
            InputStream certInputStream = getClass().getClassLoader().getResourceAsStream("chain.pem");
            if (certInputStream == null) {
                throw new FileNotFoundException("chain.pem not found in resources");
            }

            // Создание KeyStore для хранения сертификатов
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certInputStream);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("cert", cert);

            // Создание TrustManagerFactory и инициализация SSLContext
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Установка SSLContext в HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String connect() {
        try {
            // Создание URL и подключения
            URL url = new URL(API_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("RqUID", CLIENT_SECRET);
            conn.setRequestProperty("Authorization", AUTHORIZATION);

            // Отправка данных
            conn.setDoOutput(true);
            String payload = "scope=GIGACHAT_API_PERS";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Получение ответа
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Парсинг JSON-ответа с использованием Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.toString());
                return jsonResponse.toPrettyString();
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
