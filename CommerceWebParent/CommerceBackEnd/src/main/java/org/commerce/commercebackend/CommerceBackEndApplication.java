package org.commerce.commercebackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"org.commerce.commercebackend.*"})
@EntityScan({"org.commerce.commercebackend.*","org.commerce.common.*"})
public class CommerceBackEndApplication implements CommandLineRunner {


    @Autowired
    public  PasswordEncoder encoder;


    public static void main(String[] args) {
        SpringApplication.run(CommerceBackEndApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CIFRADA" + " " + encoder.encode("1234"));
    }
}
