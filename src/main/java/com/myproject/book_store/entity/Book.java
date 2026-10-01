package com.myproject.book_store.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
public record Book(@Id String bookId, String name, String price, String author, String description) {

}
