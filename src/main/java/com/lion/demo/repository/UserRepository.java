package com.lion.demo.repository;

import com.lion.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { // entity의 클래스명과 PK 타입을 제너릭으로
}
