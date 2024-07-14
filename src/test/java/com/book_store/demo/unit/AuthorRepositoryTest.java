package com.book_store.demo.unit;

import com.book_store.demo.beans.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class AuthorRepositoryTest {
    private final Logger logger = LoggerFactory.getLogger(AuthorRepositoryTest.class);
    @Autowired
    private TestEntityManager entityManager;

    private Author author_1;
    private Author author_2;
    private Author author_3;

    @Test
    public void createValidAuthorTest() {
        Author saved = entityManager.persistAndFlush(author_1);
        logger.info("saved:{}", saved);
        Author retrieved = entityManager.find(Author.class, author_1.getId());
        logger.info("retrieved:{}", retrieved);
        Assertions.assertEquals(saved, retrieved);
        Assertions.assertNotNull(retrieved);
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
        entityManager.clear();
    }
}
