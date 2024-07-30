package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book found");
            response.put("book", book.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Book not found for the id :: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book created successfully");
        response.put("book", createdBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book updated successfully");
        response.put("book", updatedBook);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Book deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(@RequestParam String query) {
        List<Book> books = bookService.searchBooks(query);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Books retrieved successfully");
        response.put("books", books);

        return ResponseEntity.ok(response);
    }
}
