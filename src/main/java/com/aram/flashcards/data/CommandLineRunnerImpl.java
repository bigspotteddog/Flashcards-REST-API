package com.aram.flashcards.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class CommandLineRunnerImpl implements CommandLineRunner {

    @Autowired
    private CategoriesLoader categoriesLoader;

    @Autowired
    private StudySessionsLoader studySessionsLoader;

    @Autowired
    private FlashcardsLoader flashcardsLoader;

    @Override
    public void run(String... args) throws Exception {
        categoriesLoader.loadCategories();
        studySessionsLoader.loadStudySessions();
        flashcardsLoader.loadFlashcards();
    }

}