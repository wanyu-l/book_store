package com.book_store.demo.integration;


import com.book_store.demo.DemoApplication;
import com.book_store.demo.beans.Author;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


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
    private Author author_1;
    private Author author_2;
    private Author author_3;
    private String url;

}
