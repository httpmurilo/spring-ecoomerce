package org.commerce.commercebackend.admin.user;

import org.commerce.common.entity.Role;
import org.commerce.common.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false )
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User newUser = new User("murilo@murilo.com","1234","murilo","edu");
        newUser.addRole(roleAdmin);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole() {

        User newUser = new User("murilo2@murilo.com","1234","murilo","edu");
        Role roleAdmin = new Role(1);
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        newUser.addRole(roleAdmin);
        newUser.addRole(roleEditor);
        newUser.addRole(roleAssistant);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserById() {
        User user = userRepository.findById(1).get();
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User newUser = userRepository.findById(1).get();
        newUser.setEnabled(true);
        newUser.setEmail("ok");

        userRepository.save(newUser);
    }

    @Test
    public void testUpdateUserRoles() {
        User newUser = userRepository.findById(1).get();
        Role roleAdmin = new Role(1);

        newUser.getRoles().remove(roleAdmin);
        newUser.addRole(roleAdmin);

        userRepository.save(newUser);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }
}
