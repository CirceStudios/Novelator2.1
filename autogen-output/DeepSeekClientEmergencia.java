package com.novelator.autogen.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DeepSeekClientEmergencia {
    private static final String API_KEY = "\" + System.getenv(\"DEEPSEEK_API_KEY\") + "\";
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private final HttpClient client;

    public DeepSeekClientEmergencia() {
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    }

    public String testConexion() {
        try {
            if (API_KEY == null || API_KEY.isEmpty() || API_KEY.equals(\"null\")) {
                return \"❌ API_KEY no configurada. Usa: export DEEPSEEK_API_KEY=tu_key\";
            }

            String requestBody = \"{\" +
                \"\\\"model\\\": \\\"deepseek-chat\\\",\" +
                \"\\\"messages\\\": [{\\\"role\\\": \\\"user\\\", \\\"content\\\": \\\"Test connection\\\"}],\" +
                \"\\\"max_tokens\\\": 10\" +
                \"}\";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header(\"Content-Type\", \"application/json\")
                .header(\"Authorization\", \"Bearer \" + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return \"✅ CONEXIÓN EXITOSA - DeepSeek operativo\";
            } else {
                return \"❌ Error HTTP: \" + response.statusCode() + \" - \" + response.body();
            }
        } catch (Exception e) {
            return \"❌ Error de conexión: \" + e.getMessage();
        }
    }

    public String analizarCodigo(String codigo) {
        try {
            String prompt = \"Analiza este código Java y sugiere mejoras específicas:\\n\\n\" + codigo;
            
            String requestBody = \"{\" +
                \"\\\"model\\\": \\\"deepseek-chat\\\",\" +
                \"\\\"messages\\\": [{\\\"role\\\": \\\"user\\\", \\\"content\\\": \\\"\" + prompt + \"\\\"}],\" +
                \"\\\"max_tokens\\\": 1000\" +
                \"}\";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header(\"Content-Type\", \"application/json\")
                .header(\"Authorization\", \"Bearer \" + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return \"Error en análisis: \" + response.statusCode() + \" - \" + response.body();
            }
        } catch (Exception e) {
            return \"Error analizando código: \" + e.getMessage();
        }
    }
}