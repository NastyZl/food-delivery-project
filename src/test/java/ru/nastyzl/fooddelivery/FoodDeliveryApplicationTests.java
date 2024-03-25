package ru.nastyzl.fooddelivery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
@SpringBootTest
class FoodDeliveryApplicationTests {

    @Test
    void contextLoads() {
    }

}
