package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
}
