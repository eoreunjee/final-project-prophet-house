package com.ssafy.home.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.home.dto.News;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public List<News> fetchNews(String query) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://openapi.naver.com/v1/search/news.json")
                    .queryParam("query", URLEncoder.encode(query, StandardCharsets.UTF_8))
                    .queryParam("display", 6)
                    .build(true) // 자동 인코딩
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode items = mapper.readTree(response.getBody()).get("items");

            List<News> result = new ArrayList<>();
            for (JsonNode item : items) {
                News dto = new News();
                dto.setTitle(item.get("title").asText());
                dto.setLink(item.get("link").asText());
                dto.setOriginallink(item.get("originallink").asText());
                dto.setDescription(item.get("description").asText());
                dto.setPubDate(item.get("pubDate").asText());
                result.add(dto);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
