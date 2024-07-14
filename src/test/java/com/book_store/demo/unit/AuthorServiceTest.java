package com.book_store.demo.unit;

import com.book_store.demo.beans.Author;
import com.book_store.demo.exception_handlers.InvalidAuthorException;
import com.book_store.demo.repositories.AuthorRepository;
import com.book_store.demo.services.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;
    @Mock
    private AuthorRepository authorRepository;

    private Author author_1;
    private Author author_2;
    private Author author_3;
    private final Logger logger = LoggerFactory.getLogger(AuthorServiceTest.class);

    @Test
    public void testInvalidAuthorCreate() {
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.createAuthor(null),
                "expected author but received null");
    }

    @Test
    public void testValidAuthorCreate() {
        try {
            String msg = authorService.createAuthor(author_1);
            logger.info(msg);
        } catch (InvalidAuthorException e) {
            fail("Should not throw exception on valid Author");
        }
        Assertions.assertDoesNotThrow(() -> authorService.createAuthor(author_2));
        Assertions.assertDoesNotThrow(() -> authorService.createAuthor(author_3));
    }

    @Test
    public void testInvalidAuthorGet() {
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.getAuthorById(0L),
                String.format("Author with id [%s] does not exist", 0L));
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.getAuthorById(11L),
                String.format("Author with id [%s] does not exist", 11L));
    }


    @Test
    public void testGetAllAuthors() {
        Assertions.assertDoesNotThrow(
                () -> authorService.getAllAuthors());
        logger.info(String.valueOf(authorService.getAllAuthors()));
        authorService.createAuthor(author_1);
        authorService.createAuthor(author_2);
        authorService.createAuthor(author_3);
        Assertions.assertDoesNotThrow(
                () -> authorService.getAllAuthors());
        int len = authorService.getAllAuthors().size();
        Assertions.assertEquals(len, 3);
    }

    @Test
    public void testInvalidAuthorUpdateName() {
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.updateAuthorName(0L, "newName"),
                String.format("Author with id [%s] does not exist", 0L));
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.updateAuthorName(11L, "newName"),
                String.format("Author with id [%s] does not exist", 11L));
    }

    @Test
    public void testInvalidAuthorUpdateBio() {
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.updateAuthorBio(0L, "newBio"),
                String.format("Author with id [%s] does not exist", 0L));
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.updateAuthorBio(11L, "newBio"),
                String.format("Author with id [%s] does not exist", 11L));
    }

    @Test
    public void testInvalidAuthorDelete() {
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.deleteAuthor(0L),
                String.format("Author with id [%s] does not exist", 0L));
        Assertions.assertThrows(InvalidAuthorException.class,
                () -> authorService.deleteAuthor(11L),
                String.format("Author with id [%s] does not exist", 11L));
    }

    @BeforeEach
    public void setAuthors() {
        author_1 = new Author();
        author_1.setId(1L);
        author_1.setName("John Doe");
        author_1.setBio("Local author");
        author_2 = new Author();
        author_2.setName("Jane Doe");
        author_2.setBio("Best Seller");
        author_3 = new Author();
        author_3.setName("Jackson Doe");
        author_3.setBio("Professional Writer");
        logger.info(author_1.toString());
    }
}
