package com.myproject.book_store.controller;

import com.myproject.book_store.dto.BookDto;
import com.myproject.book_store.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-store")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage() {
        return new ResponseEntity<>("Welcome to Book Store", HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BookDto> getBook(@PathVariable String bookId) {
        return new ResponseEntity<>(bookService.getBook(bookId), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.createBook(bookDto), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.updateBookName(bookDto), HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        bookService.deleteBookByBookId(bookId);
        return new ResponseEntity<>("Book deleted: " + bookId, HttpStatus.OK);
    }
}
