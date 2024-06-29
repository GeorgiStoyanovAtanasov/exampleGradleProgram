package com.example.demo.Services;

import com.example.demo.Clients.CatFactClient;
import com.example.demo.Entities.CatFact;
import com.example.demo.Repositories.CatFactRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CatFactService {
    private CatFactRepository catFactRepository;
    private CatFactClient catFactClient;
    @Autowired
    CatFactService(CatFactRepository catFactRepository, CatFactClient catFactClient){
        this.catFactRepository = catFactRepository;
        this.catFactClient = catFactClient;
    }
    public String getCatFact() throws IOException {
        String url = "https://catfact.ninja/fact";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result.toString());
                CatFact catFact = new CatFact(jsonNode.get("fact").asText(), jsonNode.get("fact").asText().length());
                catFactRepository.save(catFact);
                return jsonNode.get("fact").asText();
            }
        }
    }

    public String postCatFact(CatFact catFact) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/posts";

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(catFact);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonData, ContentType.APPLICATION_JSON);
            request.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return "You have sent your post request successfully!";
                } else {
                    return "Your post request sent a message:" + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
                }
            }
        }

    }
    public ResponseEntity<CatFact> getCatFactSecondMethod(){
        CatFact catFact = catFactClient.getCatFact();
        catFactRepository.save(catFact);
        return ResponseEntity.ok(catFact);
    }
}
