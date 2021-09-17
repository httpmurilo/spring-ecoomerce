package org.commerce.commercebackend.admin.user;

import org.commerce.common.entity.Role;
import org.commerce.common.entity.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false )
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreate() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User newUser = new User("murilo@murilo.com","1234","murilo","edu");
        newUser.addRole(roleAdmin);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
