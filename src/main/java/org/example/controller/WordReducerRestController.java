package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.WordReducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordReducerRestController {

    private final WordReducerService wordReducerService;

    @GetMapping("/words")
    public ResponseEntity<List<String>> findReducibleWords(@RequestParam("wordListUrl") String wordListUrl) {
        List<String> reducibleWords = wordReducerService.findReducibleWords(wordListUrl);
        return ResponseEntity.ok(reducibleWords);
    }

    @GetMapping("/test")
    public String test(){
        return "Test";
    }
}
