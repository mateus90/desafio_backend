package com.book.service;

import java.util.List;
import java.util.Optional;

import com.book.model.Book;

public interface BookService {
	
	Book save(Book book);
	
	Optional<Book> findById(Long id);

	List<Book> findAll();

}
