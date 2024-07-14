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

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;
    @Mock
    private AuthorRepository authorRepository;

    private final Author author_1 = new Author();
    private final Author author_2 = new Author();
    private final Author author_3 = new Author();
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
    public void testValidAuthorGet() {
        Assertions.assertEquals(0, authorService.getAllAuthors().size());
        authorService.createAuthor(author_1);
        // work around for changing id values in repository
        logger.info(authorService.getAllAuthors().toString());
        List<Author> authors = authorService.getAllAuthors();
        Assertions.assertDoesNotThrow(() -> authorService.getAuthorById(authors.get(0).getId()));
    }


    @Test
    public void testGetAllAuthors() {
        Assertions.assertDoesNotThrow(
                () -> authorService.getAllAuthors());
        logger.info(authorService.getAllAuthors().toString());
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
    public void testValidAuthorUpdateName() {
        String newName = "Jack Sparrow";
        Assertions.assertEquals(0, authorService.getAllAuthors().size());
        authorService.createAuthor(author_1);
        // work around for changing id values in repository
        logger.info(authorService.getAllAuthors().toString());
        List<Author> authors = authorService.getAllAuthors();
        Long targetId = authors.get(0).getId();
        Assertions.assertDoesNotThrow(() -> authorService.updateAuthorName(targetId, newName));
        Author updated = authorService.getAuthorById(targetId);
        // Since author_1 is used
        Assertions.assertNotEquals(author_1.getName(), updated.getName());
        Assertions.assertEquals(author_1.getBio(), updated.getBio());
        Assertions.assertEquals(newName, updated.getName());
    }

    @Test
    public void testValidAuthorUpdateBio() {
        String newBio = "Captain of Black Pearl";
        Assertions.assertEquals(0, authorService.getAllAuthors().size());
        authorService.createAuthor(author_1);
        // work around for changing id values in repository
        logger.info(authorService.getAllAuthors().toString());
        List<Author> authors = authorService.getAllAuthors();
        Long targetId = authors.get(0).getId();
        Assertions.assertDoesNotThrow(() -> authorService.updateAuthorBio(targetId, newBio));
        Author updated = authorService.getAuthorById(targetId);
        // Since author_1 is used
        Assertions.assertNotEquals(author_1.getBio(), updated.getBio());
        Assertions.assertEquals(author_1.getName(), updated.getName());
        Assertions.assertEquals(newBio, updated.getBio());
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

    @Test
    public void testValidAuthorDelete() {
        Mockito.when(authorRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> authorService.createAuthor(author_1));
        // work around for changing id values in repository
        List<Author> authors = authorService.getAllAuthors();
        logger.info(authorService.getAllAuthors().toString());
        Assertions.assertDoesNotThrow(() -> authorService.deleteAuthor(authors.get(0).getId()));
        Assertions.assertEquals(authors.size() - 1, authorService.getAllAuthors().size());
    }

    @BeforeEach
    public void setAuthors() {
        author_1.setId(1L);
        author_1.setName("John Doe");
        author_1.setBio("Local author");
        author_2.setId(2L);
        author_2.setName("Jane Doe");
        author_2.setBio("Best Seller");
        author_3.setId(3L);
        author_3.setName("Jackson Doe");
        author_3.setBio("Professional Writer");
    }
}
