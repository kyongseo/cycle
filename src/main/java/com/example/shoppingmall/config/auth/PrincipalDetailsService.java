package com.example.shoppingmall.config.auth;


import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Controller
@Service
public class PrincipalDetailsService implements UserDetailsService {

    //@Autowired(required = true)
    private final UserRepository userRepository;
    //private UserRepository userRepository;

    /* username이 DB에 있는지 확인 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);
//        User userEntity = userRepository.findByUsername(username).orElseThrow(() ->
//                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username));

        if(userEntity == null) {
            return null;
        } else {
            return new PrincipalDetails(userEntity);
        }
    }
}