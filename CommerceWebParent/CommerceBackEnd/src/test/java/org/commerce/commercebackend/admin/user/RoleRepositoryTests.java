package org.commerce.commercebackend.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.commerce.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false )
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    public void TestCreatedFirstRole() {
        Role roleAdmin = new Role("Admin","manage everything");
        Role savedRole = repository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles() {
        Role roleSalesPerson = new Role("Salesperson","manage product price,customer, shipping, orders and sales report");
        Role roleEditor = new Role("Editor","manage categories, brands,products,articles and menus");
        Role roleShipper = new Role("Shipper","view products, view orders and update order status");
        Role roleAssistant = new Role("Assistant","manage questions and reviews");
        repository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));

    }
}
