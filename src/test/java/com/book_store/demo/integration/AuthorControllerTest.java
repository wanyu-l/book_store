package com.book_store.demo.integration;

import com.book_store.demo.beans.Author;
import com.book_store.demo.exception_handlers.InvalidAuthorException;
import com.book_store.demo.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.fail;

public class AuthorControllerTest {
    @Autowired
    private AuthorService authorService;

    private Author author_1;
    private Author author_2;
    private Author author_3;

    @Test
    public void InvalidAuthor() {
        try {
            authorService.createAuthor(null);
            fail("Should  throw exception on null author");
        } catch (InvalidAuthorException e) {

        }
    }

    @BeforeEach
    public void setAuthors() {
        author_1 = new Author();
        author_1.setName("Jk Rowling");
        author_1.setBio("UK author");
        author_2 = new Author();
        author_2.setName("RR Martin");
        author_2.setBio("Best Seller");
        author_3 = new Author();
        author_3.setName("John Doe");
        author_3.setBio("Professional Writer");
        authorService = new AuthorService();
    }
}
