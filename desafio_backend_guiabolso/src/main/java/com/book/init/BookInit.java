package com.book.init;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.book.ApplicationContextHolder;
import com.book.model.Book;
import com.book.service.BookService;

@RestController
public class BookInit {

	private static BookInit instance;

	private static final Logger logger = LoggerFactory.getLogger(BookInit.class);
	private static final String URI = "https://kotlinlang.org/docs/books.html";
	private static final String REGEX_ISBN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
	private static final String REGEX_REMOVE_TAG = "<[^>]*>";
	private static final String ISBN_DEFAULT = "Unavailable";

	public static synchronized BookInit getInstance() {
		if (instance == null) {
			instance = new BookInit();
		}
		return instance;
	}

	public void populateDatabase() {
		try {
			logger.info("Beginning of data entry");
			BookService bookService = ApplicationContextHolder.getContext().getBean(BookService.class);

			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(URI, String.class);
			Document doc = Jsoup.parse(result);
			Elements elementsH2 = doc.select("h2");
			Iterator<Element> iterator = elementsH2.iterator();
			while (iterator.hasNext()) {
				String titleBook = "";
				try {
					Element h2 = iterator.next();
					titleBook = h2.html();
					Element div = h2.nextElementSibling();
					String language = div.html();

					StringBuilder description = new StringBuilder();
					Element p = div.nextElementSibling().nextElementSibling();
					String uriBook = "";
					while (p != null && p.tagName().equals("p")) {
						if (StringUtils.isEmpty(uriBook)) {
							uriBook = p.getElementsByTag("a").attr("href");
						}
						description.append(p.toString());
						p = p.nextElementSibling();
					}
					String isbn = searchISBN(uriBook);
					Book book = new Book(titleBook, description.toString(), language.toUpperCase(), isbn);
					bookService.save(book);
				} catch (Exception e) {
					logger.error("Error to insert a book " + titleBook + " in the database");
				}
			}
		} catch (Exception e) {
			logger.error("Error to populate in the database");
		}
		logger.info("End of data entry");
	}

	private String searchISBN(String uriBook) {
		try {
			Pattern pattern = Pattern.compile(REGEX_ISBN);
			Matcher matcher;
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uriBook, String.class);
			Document doc = Jsoup.parse(result);
			Elements elements = doc.select("*:containsOwn(isbn)");
			if (elements == null || elements.isEmpty()) {
				elements = doc.select("*:containsOwn(Stok Kodu)");
				if (elements == null || elements.isEmpty()) {
					return ISBN_DEFAULT;
				}
			}
			Element e = elements.iterator().next();
			if (e == null || StringUtils.isEmpty(e.html())) {
				return ISBN_DEFAULT;
			}
			String text = e.html().replaceAll(REGEX_REMOVE_TAG, "");
			matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group();
			}
			Element nextElement = e.nextElementSibling();
			if (nextElement != null && !StringUtils.isEmpty(nextElement.html())) {
				text = nextElement.html().replaceAll(REGEX_REMOVE_TAG, "");
				matcher = pattern.matcher(text);
				if (matcher.find()) {
					return matcher.group();
				}
			}
			Element parent = e.parent();
			if (parent != null && !StringUtils.isEmpty(parent.html())) {
				text = parent.html().replaceAll(REGEX_REMOVE_TAG, "");
				matcher = pattern.matcher(text);
				if (matcher.find()) {
					return matcher.group();
				}
			}
			Element lastTry = nextElement.nextElementSibling();
			if (lastTry != null && !StringUtils.isEmpty(lastTry.html())) {
				text = lastTry.html().replaceAll(REGEX_REMOVE_TAG, "");
				matcher = pattern.matcher(text);
				if (matcher.find()) {
					return matcher.group();
				}
			}

		} catch (Exception e) {
			logger.error("Error to search isbn from uri: " + uriBook);
		}
		return ISBN_DEFAULT;
	}

}
