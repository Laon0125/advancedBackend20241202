package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired private BookRepository bookRepository;

    @Test
    void testSaveAndFindBook() {
        Book book = new Book(0,"title","author","company",20000,"ImageUrl","summary");

        bookRepository.save(book);

        List<Book> bookList = bookRepository.findAll();
        int size = bookList.size();
        System.out.println("size = " + size );

        Assertions.assertThat(bookList).hasSize(1);
        Assertions.assertThat(bookList.get(0).getTitle()).isEqualTo("title");
    }

    @Test
    void testSaveAndFindBookByTitle() {
        Book book = new Book(0,"title","author","company",20000,"ImageUrl","summary");
        Book book2 = new Book(0,"title","author","company",20000,"ImageUrl","summary");

        bookRepository.save(book);
        bookRepository.save(book2);

        Pageable pageable = PageRequest.of(0,10);
        List<Book> bookList = bookRepository.findByTitleContaining("title",pageable).getContent();
        int size = bookList.size();
        System.out.println("size = " + size );

        Assertions.assertThat(bookList).hasSize(2);
        Assertions.assertThat(bookList.get(0).getAuthor()).isEqualTo("author");
        Assertions.assertThat(bookList.get(0).getPrice()).isEqualTo(20000);
    }

}
