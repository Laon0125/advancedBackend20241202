package com.lion.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cid;
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
    @ManyToOne
    @JoinColumn(name = "bid")
    private Book book;
    private int quantity;

}
