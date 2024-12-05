package com.lion.demo.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

//@Getter
//@Setter
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id     //pk
    private String uid;
    private String pwd;
    private String uname;
    private String email;
    public LocalDate registerDate;
    private String role;
    private String provider;
    private String profileUrl;


}
