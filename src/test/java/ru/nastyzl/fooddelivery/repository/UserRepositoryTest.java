package ru.nastyzl.fooddelivery.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nastyzl.fooddelivery.model.AddressEntity;
import ru.nastyzl.fooddelivery.model.AdminEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository<UserEntity> userRepository;

    @Test
    public void givenNewUser_whenSave_thenSuccess() {
        AdminEntity admin = new AdminEntity();
        admin.setUsername("test");
        admin.setEmail("test@gmial.com");
        admin.setFirstName("first name");
        admin.setLastName("last name");
        admin.setPassword("123");
        admin.setPhone("89105038707");

        AdminEntity insertedAdmin = userRepository.save(admin);

        assertThat(entityManager.find(AdminEntity.class, insertedAdmin.getId())).isEqualTo(admin);
    }

    @Test
    void givenUserCreated_whenUpdate_thenSuccess() {
        AddressEntity newAddress = new AddressEntity();
        newAddress.setApartment("1");
        newAddress.setCity("Рязань");
        newAddress.setEntrance(1);
        newAddress.setFloor(3);
        newAddress.setStreetName("Ленина");
        newAddress.setStreetNumber("13");


        VendorEntity newVendor = new VendorEntity();
        newVendor.setUsername("test");
        newVendor.setEmail("vendor1@gmial.com");
        newVendor.setFirstName("first name");
        newVendor.setLastName("last name");
        newVendor.setPassword("123");
        newVendor.setPhone("89105038707");
        newVendor.setAddress(newAddress);

        entityManager.persist(newVendor);
        String newEmail = "new_vendor@gmail.com";
        newVendor.setEmail(newEmail);

        userRepository.save(newVendor);
        assertThat(entityManager.find(UserEntity.class, newVendor.getId()).getEmail()).isEqualTo(newEmail);
    }

    @Test
    void givenUserCreated_whenFindById_thenSuccess() {
        VendorEntity newVendor = new VendorEntity();
        newVendor.setUsername("test-1");
        newVendor.setEmail("vendor@gmial.com");
        newVendor.setFirstName("first name");
        newVendor.setLastName("last name");
        newVendor.setPassword("123");
        newVendor.setPhone("89105038707");
        entityManager.persist(newVendor);
        Optional<UserEntity> retrievedUser = userRepository.findById(newVendor.getId());
        assertThat(retrievedUser).contains(newVendor);
    }

    @Test
    void givenUserCreated_whenDelete_thenSuccess() {
        AddressEntity newAddress = new AddressEntity();
        newAddress.setApartment("1");
        newAddress.setCity("Рязань");
        newAddress.setEntrance(1);
        newAddress.setFloor(3);
        newAddress.setStreetName("Ленина");
        newAddress.setStreetNumber("13");


        VendorEntity newVendor = new VendorEntity();
        newVendor.setUsername("test");
        newVendor.setEmail("vendor1@gmial.com");
        newVendor.setFirstName("first name");
        newVendor.setLastName("last name");
        newVendor.setPassword("123");
        newVendor.setPhone("89105038707");
        newVendor.setAddress(newAddress);
        entityManager.persist(newVendor);
        userRepository.delete(newVendor);
        assertThat(entityManager.find(UserEntity.class, newVendor.getId())).isNull();
    }

//    @Test
//    void findByUsername() {
//        String name = "admin";
//        Optional<UserEntity> user = userRepository.findByUsername(name);
//        assertThat(user).isNotNull();
//    }
}