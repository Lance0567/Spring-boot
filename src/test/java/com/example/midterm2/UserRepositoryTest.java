package com.example.midterm2;

import com.example.midterm2.Entity.User;
import com.example.midterm2.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("lanceesurena@gmail.com");
        user.setUsername("Lance");
        user.setPassword("lance0909");

        User saveUser = repo.save(user);
        User existUser = entityManager.find(User.class, saveUser.getId());
        assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
    }
}
