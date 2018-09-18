package com.book.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.book.model.Book;
import com.book.model.Books;
import com.book.service.BookService;

@RestController
public class BookController {
	@Autowired
	private BookService bookService;

	@PostMapping("/books")
	public ResponseEntity<Object> createBook(@RequestBody Book book) {
		Book savedBook = bookService.save(book);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBook.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/books/{id}")
	public Book retrieveBook(@PathVariable long id) {
		Optional<Book> book = bookService.findById(id);

		if (!book.isPresent())
			throw new IllegalArgumentException("id-" + id);

		return book.get();
	}

	@GetMapping("/books")
	public Books retrieveAllBooks() {
		List<Book> listBook = bookService.findAll();
		Books books = new Books(listBook != null ? listBook.size() : 0, listBook);
		return books;
	}

}
