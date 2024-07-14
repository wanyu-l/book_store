package com.book_store.demo.integration;


import com.book_store.demo.DemoApplication;
import com.book_store.demo.beans.Author;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DemoApplication.class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorEndPointsTest {
    private final Logger logger = LoggerFactory.getLogger(AuthorEndPointsTest.class);
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private Author author_1 = new Author();
    private Author author_2 = new Author();
    private Author author_3 = new Author();
    private String postUrl;
    private String getUrl;
    private String deleteUrl;
    private String listUrl;
    private String updateNameUrl;
    private String updateBioUrl;

    private String getPathUrl(String url, Long id) {
        return url + "/" + id;
    }

    private void createAuthor(Author author) {
        logger.info("creating...{}", author);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(postUrl, author, String.class);
        logger.info(responseEntity.toString());
        String response = testRestTemplate.postForObject(postUrl, author, String.class);
        logger.info(response);
    }

    private void deleteAuthor(Long id) {
        String endPoint = getPathUrl(deleteUrl, id);
        testRestTemplate.delete(endPoint);
        logger.info("Deleted Author {}", id);
    }

    @BeforeAll
    public void init() {
        getUrl = "http://localhost:" + port + "/authors";
        deleteUrl = "http://localhost:" + port + "/authors/delete";
        listUrl = "http://localhost:" + port + "/authors/list";
        postUrl = "http://localhost:" + port + "/authors/create";
        updateNameUrl = "http://localhost:" + port + "/authors/updateName";
        updateBioUrl = "http://localhost:" + port + "/authors/updateBio";
    }

    @BeforeEach
    public void setupAuthors() {
        author_1.setId(1L);
        author_1.setName("Jack Sparrow");
        author_1.setBio("Pirate of the Seven Seas");
        author_2.setId(2L);
        author_2.setName("John");
        author_2.setBio("The boy");
        author_3.setId(3L);
        author_3.setName("Joseph Joe Star");
        author_3.setBio("The honoured one");
    }

    @Test
    public void testCreateAuthor() {
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(postUrl, author_1, String.class);
        logger.info(responseEntity.toString());
        String response = testRestTemplate.postForObject(postUrl, author_1, String.class);
        logger.info(response);
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 200);
        Assertions.assertEquals(responseEntity.getBody(), "Author created successfully");
    }

    @Test
    public void testDeleteAuthor() {
        createAuthor(author_1);
        // work around for changing id
        List<Author> effectiveAuthors = List.of(testRestTemplate.getForObject(listUrl,
                Author[].class));
        Long targetId = effectiveAuthors.get(0).getId();
        String endPoint = getPathUrl(getUrl, targetId);
        Author received = testRestTemplate.getForObject(endPoint, Author.class, author_1);
        logger.info("before delete:{}", received.toString());
        deleteAuthor(targetId);
        Author receivedAfterDelete = testRestTemplate.getForObject(endPoint, Author.class, author_1);
        logger.info("after delete:{}", receivedAfterDelete.toString());
        Assertions.assertNotNull(receivedAfterDelete);
        Assertions.assertNull(receivedAfterDelete.getId());
        Assertions.assertNull(receivedAfterDelete.getName());
        Assertions.assertNull(receivedAfterDelete.getBio());
    }

    @Test
    public void testCreateInvalidAuthor() {
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(postUrl, null, String.class);
        logger.info(responseEntity.toString());
        String response = testRestTemplate.postForObject(postUrl, null, String.class);
        logger.info(response);
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 500);
        Assertions.assertNotEquals(responseEntity.getBody(), "Author created successfully");
    }

    @Test
    public void testGetAuthor() {
        createAuthor(author_1);
        // work around for changing id
        List<Author> effectiveAuthors = List.of(testRestTemplate.getForObject(listUrl,
                Author[].class));
        String endPoint = getPathUrl(getUrl, effectiveAuthors.get(0).getId());
        Author received = testRestTemplate.getForObject(endPoint, Author.class, effectiveAuthors.get(0));
        Assertions.assertEquals(effectiveAuthors.get(0).getName(), received.getName());
        Assertions.assertEquals(effectiveAuthors.get(0).getBio(), received.getBio());
    }

    @Test
    public void testUpdateAuthor() {
        createAuthor(author_1);
        // work around for changing id
        List<Author> effectiveAuthors = List.of(testRestTemplate.getForObject(listUrl,
                Author[].class));
        Long targetId = effectiveAuthors.get(0).getId();
        String getEndPoint = getPathUrl(getUrl, targetId);
        Author curr = testRestTemplate.getForObject(getEndPoint, Author.class, targetId);
        logger.info((curr.toString()));
        String newName = "Peter Parker";
        curr.setName(newName);
        HttpEntity<Author> requestEntity = new HttpEntity<>(curr);
        testRestTemplate.exchange(getPathUrl(updateNameUrl, targetId), HttpMethod.PUT, requestEntity, Void.class);
//        testRestTemplate.exchange("http://localhost:8080/authors/updateName/1?newName=adw", HttpMethod.PUT, requestEntity, Void.class);
//        "http://localhost:8080/authors/updateName/1?newName=adw"
        Author updatedName = testRestTemplate.getForObject(getEndPoint, Author.class, targetId);
        logger.info((updatedName.toString()));
        String newBio = "Aspiring Sci-fi author";
        curr.setBio(newBio);
        testRestTemplate.put(getPathUrl(updateBioUrl, targetId), Author.class, curr);
        Author updatedBio = testRestTemplate.getForObject(getEndPoint, Author.class, targetId);
        logger.info(updatedBio.toString());
        logger.info("curr:{}", curr);
    }

    @Test
    public void testGetAllAuthor() {
        List<Author> beforeAdd = List.of(testRestTemplate.getForObject(listUrl,
                Author[].class));
        Assertions.assertEquals(0, beforeAdd.size());
        createAuthor(author_1);
        createAuthor(author_2);
        List<Author> afterAdd = List.of(testRestTemplate.getForObject(listUrl,
                Author[].class));
        logger.info("{} authors exist for now", afterAdd.size());
        for (Author author : afterAdd) {
            logger.info(author.toString());
            Assertions.assertNotNull(author.getId());
            Assertions.assertNotNull(author.getName());
            Assertions.assertNotNull(author.getBio());
        }
        Assertions.assertEquals(2, afterAdd.size());
    }
}
