package com.myproject.book_store.repository;

import com.myproject.book_store.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

}
