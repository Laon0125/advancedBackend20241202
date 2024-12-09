package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.Cart;
import com.lion.demo.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CartRepositoryTest {
    @Autowired private CartRepository cartRepository;

    @Test
    void testSaveCart() {
        User user = User.builder().uid("test").uname("테스트").build();
        Book book = Book.builder().title("title").price(20000).build();
        Cart cart = Cart.builder()
                .user(user).book(book).quantity(3)
                .build();

        Cart saveCart = cartRepository.save(cart);

        Assertions.assertThat(saveCart.getCid()).isNotNull();
        Assertions.assertThat(saveCart.getUser().getUid()).isEqualTo("test");
        Assertions.assertThat(saveCart.getBook().getPrice()).isEqualTo(20000);
        Assertions.assertThat(saveCart.getQuantity()).isEqualTo(3);
    }

    @Test
    void testFindCart() {
        User user = User.builder().uid("test").uname("테스트").build();
        Book book = Book.builder().title("title").price(20000).build();
        Cart cart = Cart.builder()
                .user(user).book(book).quantity(3)
                .build();

        Cart savedCart = cartRepository.save(cart);

        Cart foundCart = cartRepository.findById(savedCart.getCid()).orElse(null);


        Assertions.assertThat(foundCart.getQuantity()).isEqualTo(3);
        Assertions.assertThat(foundCart.getCid()).isNotNull();
        Assertions.assertThat(foundCart.getUser().getUid()).isEqualTo("test");
        Assertions.assertThat(foundCart.getBook().getPrice()).isEqualTo(20000);
    }




}
