package com.lion.demo.security;
import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    @Autowired private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUid(username);
        if (user == null) {
            log.warn("로그인 실패: 아이디가 존제하지 않습니다 (username: " + username + ")");
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다");
        }
        log.info("Login 시도: " + user.getUid());
        return new MyUserDetails(user);
    }
}