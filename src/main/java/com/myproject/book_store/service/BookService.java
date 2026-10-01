package com.myproject.book_store.service;

import com.myproject.book_store.dto.BookDto;

import java.util.List;

public interface BookService {

    public BookDto getBook(String bookId);

    public List<BookDto> getBooks();

    public BookDto createBook(BookDto bookDto);


    public BookDto updateBookName(BookDto bookDto);

    public void deleteBookByBookId(String bookId);



}
