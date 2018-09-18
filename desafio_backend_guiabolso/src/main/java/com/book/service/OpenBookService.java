package com.book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.model.Book;
import com.book.repository.BookRepo;

@Service
public class OpenBookService implements BookService {

	@Autowired
	private BookRepo bookRepo;

	@Override
	public Book save(Book book) {
		return bookRepo.save(book);
	}

	@Override
	public Optional<Book> findById(Long id) {
		return bookRepo.findById(id);
	}
	
	@Override
	public List<Book> findAll() {
		return bookRepo.findAll();
	}

}
