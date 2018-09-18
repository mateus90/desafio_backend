package com.book.model;

import java.io.Serializable;
import java.util.List;

public class Books implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numberBooks;
	private List<Book> books;

	public Books(int numberBooks, List<Book> books) {
		this.numberBooks = numberBooks;
		this.books = books;
	}

	public int getNumberBooks() {
		return numberBooks;
	}

	public void setNumberBooks(int numberBooks) {
		this.numberBooks = numberBooks;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
