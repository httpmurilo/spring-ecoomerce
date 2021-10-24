package org.commerce.commercebackend.admin.security;

import org.commerce.commercebackend.admin.user.UserRepository;
import org.commerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);

        if (user != null) {
            return new ShopMeUserDetails(user);
        }
        throw  new UsernameNotFoundException("Could not find user with email" + email);

    }
}
