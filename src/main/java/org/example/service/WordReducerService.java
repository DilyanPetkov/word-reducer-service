package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class WordReducerService {

    private static final Set<String> SINGLE_LETTER_WORDS = Set.of("A", "I");

    public List<String> findReducibleWords(String wordListUrl) {
        log.info("Loading dictionary from: {}", wordListUrl);
        Set<String> dictionary = loadWords(wordListUrl);
        log.info("Loaded {} words.", dictionary.size());

        List<String> reducibleWords = new ArrayList<>();
        List<String> nineLetterWords = dictionary.stream()
                .filter(w -> w.length() == 9)
                .toList();
        log.info("Found {} 9-letter words.", nineLetterWords.size());

        for (String word : nineLetterWords) {
            if (isReducible(word, dictionary)) {
                reducibleWords.add(word);
                log.info("Found reducible word: {}", word);
            }
        }

        //TEST

        log.info("Total reducible 9-letter words found: {}", reducibleWords.size());
        return reducibleWords;
    }

    private Set<String> loadWords(String urlStr) {
        Set<String> words = new HashSet<>();
        try {
            URL url = new URL(urlStr);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String clean = line.trim().toUpperCase();
                    if (!clean.isEmpty() && clean.matches("[A-Z]+")) {
                        words.add(clean);
                    }
                }
            }
            words.addAll(SINGLE_LETTER_WORDS);
            log.info("Finished reading words. Total words including A/I: {}", words.size());
        } catch (Exception e) {
            log.error("Failed to load words from URL: {}", urlStr, e);
            throw new RuntimeException("Failed to load words from URL: " + e.getMessage(), e);
        }
        return words;
    }

    private boolean isReducible(String word, Set<String> dictionary) {
        if (SINGLE_LETTER_WORDS.contains(word)) {
            log.debug("Reached base case with word: {}", word);
            return true;
        }
        if (!dictionary.contains(word)) {
            log.debug("Word '{}' is not in dictionary. Stopping.", word);
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            String reducedWord = new StringBuilder(word).deleteCharAt(i).toString();
            if (dictionary.contains(reducedWord)) {
                log.debug("Reduced '{}' to '{}', continuing recursion.", word, reducedWord);
                if (isReducible(reducedWord, dictionary)) {
                    return true;
                }
            }
        }
        return false;
    }
}
