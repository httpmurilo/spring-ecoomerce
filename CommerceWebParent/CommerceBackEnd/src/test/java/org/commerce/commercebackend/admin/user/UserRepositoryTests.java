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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        User newUser = new User("murilo2@murilo.com","1234","murilo","edu");
        newUser.addRole(roleAdmin);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole() {

        User newUser = new User("murilo222@murilo.com","1234","murilo","edu");
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
        User user = userRepository.findById(5).get();
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User newUser = userRepository.findById(6).get();
        newUser.setEnabled(true);
        newUser.setEmail("ok");

        userRepository.save(newUser);
    }

    @Test
    public void testUpdateUserRoles() {
        User newUser = userRepository.findById(5).get();
        Role roleAdmin = new Role(1);

        newUser.getRoles().remove(roleAdmin);
        newUser.addRole(roleAdmin);

        userRepository.save(newUser);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 6;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetInvalidUserByEmail() {
        String email = "abc@def.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNull();
    }

    @Test
    public void testGetUserByEmail() {
        String email = "murilo2@murilo.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountByInvalidId() {
        Integer id = 100;
        Long countById = userRepository.countById(id);

        assertThat(countById).isZero();
    }

    @Test
    public void testCountById() {
        Integer id = 5;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 5;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 5;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page =  userRepository.findAll(pageable);

        List<User> listUsers =  page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsersByName() {
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page =  userRepository.findAll(keyword, pageable);

        List<User> listUsers =  page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
