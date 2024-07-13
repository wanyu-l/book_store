package com.book_store.demo.services;


import com.book_store.demo.beans.Author;
import com.book_store.demo.exception_handlers.InvalidAuthorException;
import com.book_store.demo.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public String createAuthor(Author author) throws InvalidAuthorException {
        if (Objects.isNull(author)) {
            throw new InvalidAuthorException("expected author but received null");
        }
        authorRepository.save(author);
        return "Author created successfully";
    }

    public Author getAuthorById(Long id) throws InvalidAuthorException {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isEmpty()) {
            throw new InvalidAuthorException(String.format("Author with id [%s] does not exist", id));
        }
        return author.get();
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public String updateAuthorName(Long id, String newName) throws InvalidAuthorException {
        if (!authorRepository.existsById(id)) {
            throw new InvalidAuthorException(String.format("Author with id [%s] does not exist", id));
        }
        authorRepository.getReferenceById(id).setName(newName);
        return String.format("Successfully updated author: [%s]", authorRepository.getReferenceById(id));
    }

    public String updateAuthorBio(Long id, String newBio) throws InvalidAuthorException {
        if (!authorRepository.existsById(id)) {
            throw new InvalidAuthorException(String.format("Author with id [%s] does not exist", id));
        }
        authorRepository.getReferenceById(id).setBio(newBio);
        return String.format("Successfully updated author: [%s]", authorRepository.getReferenceById(id));
    }

    public String deleteAuthor(Long id) throws InvalidAuthorException {
        if (!authorRepository.existsById(id)) {
            throw new InvalidAuthorException(String.format("Author with id [%s] does not exist", id));
        }
        authorRepository.deleteById(id);
        return String.format("Successfully deleted author with id [%s]", id);
    }

}
