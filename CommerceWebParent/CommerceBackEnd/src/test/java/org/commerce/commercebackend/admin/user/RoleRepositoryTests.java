package org.commerce.commercebackend.admin.user;

import org.commerce.common.entity.Role;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    public void TestCreatedFirstRole() {
        Role roleAdmin = new Role("Admin","manage everything");
        Role savedRole = repository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }
}
