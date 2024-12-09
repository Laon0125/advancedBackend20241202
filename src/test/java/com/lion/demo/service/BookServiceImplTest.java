package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class BookServiceImplTest {
    @Mock private BookRepository bookRepository;
    @InjectMocks private BookServiceImpl bookService; // bookServiceImpl 객체 생성후 bookRepository 주입


    @BeforeEach //테스트를 실행할때마다 수행
    void setup() {
        MockitoAnnotations.openMocks(this);  // 테스트 초기화
    }

    @Test
    void testFindBookById() {
        Book book = Book.builder()
                .bid(999L).title("title").price(20000)
                .build();
        when(bookRepository.findById(999L)).thenReturn(Optional.of(book));

        //when
        Book foundBook = bookService.findByBid(999L);

        //then
        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getTitle()).isEqualTo("title");
        Assertions.assertThat(foundBook.getPrice()).isEqualTo(20000);
    }

    @Test
    void testGetBooksByPage() {
        Book book1 = Book.builder()
                .bid(999L).title("title1").price(210000)
                .build();
        Book book2 = Book.builder()
                .bid(999L).title("title2").price(220000)
                .build();
        Book book3 = Book.builder()
                .bid(999L).title("title3").price(230000)
                .build();
        Book book4 = Book.builder()
                .bid(999L).title("title4").price(240000)
                .build();
        Book book5 = Book.builder()
                .bid(999L).title("title5").price(250000)
                .build();
        Pageable pageable = PageRequest.of(0,10);
        List<Book> bookList = Arrays.asList(book1, book2, book3, book4, book5);
        Page<Book> bookPage = new PageImpl<>(bookList,pageable,bookList.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<Book> foundBookList = bookService.getBooksByPage(1);


        Assertions.assertThat(foundBookList).hasSize(5);
        Assertions.assertThat(foundBookList.get(0).getTitle()).isEqualTo("title1");
        Assertions.assertThat(foundBookList.get(4).getPrice()).isEqualTo(250000);
    }

    @Test
    void testGetBookList() {
        Book book1 = Book.builder()
                .bid(999L).title("title1").author("author1").company("company1").summary("summary1")
                .build();
        Book book2 = Book.builder()
                .bid(999L).title("title2").author("author2").company("company2").summary("summary2")
                .build();
        Book book3 = Book.builder()
                .bid(999L).title("title3").author("author3").company("company3").summary("summary3")
                .build();
        Book book4 = Book.builder()
                .bid(999L).title("title4").author("author4").company("company4").summary("summary4")
                .build();
        Book book5 = Book.builder()
                .bid(999L).title("title5").author("author5").company("panycom5").summary("summary5")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = Arrays.asList(book1, book2, book3, book4, book5);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        when(bookRepository.findByTitleContaining("title", pageable)).thenReturn(bookPage);
        when(bookRepository.findByAuthorContaining("author", pageable)).thenReturn(bookPage);
        when(bookRepository.findByCompanyContaining("company", pageable)).thenReturn(bookPage);
        when(bookRepository.findBySummaryContaining("summary", pageable)).thenReturn(bookPage);

        List<Book> foundBooksByTitle = bookService.getBookList(1, "title", "title");
        List<Book> foundBooksByAuthor = bookService.getBookList(1, "author", "author");
        List<Book> foundBooksByCompany = bookService.getBookList(1, "company", "company");
        List<Book> foundBooksBySummary = bookService.getBookList(1, "summary", "summary");

        Assertions.assertThat(foundBooksByTitle).hasSize(5);
        Assertions.assertThat(foundBooksByTitle.get(0).getTitle()).isEqualTo("title1");
        Assertions.assertThat(foundBooksByCompany).hasSize(5);
        Assertions.assertThat(foundBooksByAuthor).hasSize(5);
        Assertions.assertThat(foundBooksBySummary).hasSize(5);

    }
    @Test void testInsertionBook() {
        Book book = Book.builder().bid(999L).title("title").price(20000).build();
        bookService.updateBook(book);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).save(book);

        verify(bookRepository, times(1)).deleteById(1L);

    }

}
