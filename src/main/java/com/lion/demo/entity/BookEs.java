package com.lion.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEs {
    @Id
    private String bookId;  // uuid
    private String title;
    private String author;
    private String company;
    private int price;
    private  String imageUrl;
    private String summary;
}
