package com.myproject.book_store.service.impl;

import com.myproject.book_store.dto.BookDto;
import com.myproject.book_store.entity.Book;
import com.myproject.book_store.exception.ResourceNotFoundException;
import com.myproject.book_store.mapper.BookMapper;
import com.myproject.book_store.repository.BookRepository;
import com.myproject.book_store.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDto getBook(String bookId) {
        return BookMapper.toDto(findOrThrow(bookId));
    }

    @Override
    public List<BookDto> getBooks() {
        return bookRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookRepository.insert(BookMapper.toEntity(bookDto));
        return BookMapper.toDto(book);
    }

    @Override
    public BookDto updateBookName(BookDto bookDto) {
        if (bookDto.bookId() == null) {
            throw new IllegalArgumentException("bookId is required");
        }
        Book existing = findOrThrow(bookDto.bookId());
        Book updated = new Book(existing.bookId(), bookDto.name(), existing.price(),
                existing.author(), existing.description());
        return BookMapper.toDto(bookRepository.save(updated));
    }

    @Override
    public void deleteBookByBookId(String bookId) {
        findOrThrow(bookId);
        bookRepository.deleteById(bookId);
    }

    private Book findOrThrow(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book " + bookId + " not found"));
    }
}
