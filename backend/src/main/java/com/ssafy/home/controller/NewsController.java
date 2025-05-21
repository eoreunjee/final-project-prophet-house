package com.ssafy.home.controller;

import com.ssafy.home.dto.News;
import com.ssafy.home.service.NewsService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<News>> getNews(@RequestParam String query) {
        return ResponseEntity.ok(newsService.fetchNews(query));
    }
}
