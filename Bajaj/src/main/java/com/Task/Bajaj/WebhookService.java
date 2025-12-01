package com.Task.Bajaj;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    private String solveQuestion2() {
        return "SELECT " +
                "d.DEPARTMENT_NAME, " +
                "AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, " +
                "GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) " +
                "             ORDER BY e.FIRST_NAME, e.LAST_NAME " +
                "             SEPARATOR ', ') AS EMPLOYEE_LIST " +
                "FROM DEPARTMENT d " +
                "JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT " +
                "JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                "WHERE p.AMOUNT > 70000 " +
                "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
                "ORDER BY d.DEPARTMENT_ID DESC " +
                "LIMIT 10;";
    }

    private void submitFinalQuery(String url, String token, String query) {

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", token);
        headers.set(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/json");

        Map<String, String> json = new HashMap<>();
        json.put("finalQuery", query);

        org.springframework.http.HttpEntity<Map<String, String>> entity =
                new org.springframework.http.HttpEntity<>(json, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Webhook Response: " + response.getBody());
    }

    public void startProcess() {

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> body = new HashMap<>();
        body.put("name", "K Yashwanth Vignesh");
        body.put("regNo", "22BLC1194");
        body.put("email", "yash.vignesh.k@gmail.com");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");

        String finalQuery;

        finalQuery = solveQuestion2();


        submitFinalQuery(webhookUrl, accessToken, finalQuery);
    }
}
