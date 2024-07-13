package com.book_store.demo.controllers;


import com.book_store.demo.beans.Author;
import com.book_store.demo.exception_handlers.InvalidAuthorException;
import com.book_store.demo.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping("/create")
    public String createAuthor(@RequestBody Author author) {
        String message;
        try {
            message = authorService.createAuthor(author);
        } catch (InvalidAuthorException e) {
            return String.format("Failed to create author: [%s]", e.getMessage());
        }
        return message;
    }

    @GetMapping("/list")
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAllAuthors(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PutMapping("/updateName/{id}")
    public String updateAuthorName(@PathVariable Long id, @RequestParam String newName) {
        String message;
        try {
            message = authorService.updateAuthorName(id, newName);
        } catch (InvalidAuthorException e) {
            return String.format("Failed to update name: [%s]", e.getMessage());
        }
        return message;
    }
    @PutMapping("/updateBio/{id}")
    public String updateAuthorBio(@PathVariable Long id, @RequestParam String newBio) {
        String message;
        try {
            message = authorService.updateAuthorBio(id, newBio);
        } catch (InvalidAuthorException e) {
            return String.format("Failed to update bio: [%s]", e.getMessage());
        }
        return message;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        String message;
        try {
            message = authorService.deleteAuthor(id);
        } catch (InvalidAuthorException e) {
            return String.format("Failed to delete author: [%s]", e.getMessage());
        }
        return message;
    }
}
