package ru.nastyzl.fooddelivery.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
class DishRepositoryTest {
    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void testCreateNewDish() {
        DishEntity dish = new DishEntity();
        dish.setDishName("test");
        dish.setDescription("test description");
        dish.setCurrentPrice(2000d);
        dish.setQuantity(4);
        dish.setVendorEntity(entityManager.getEntityManager()
                .createQuery("select vendor from VendorEntity vendor where vendor.id=:id", VendorEntity.class)
                .setParameter("id", 162L)
                .getSingleResult());
        dish.setImgPath("default.png");

        DishEntity dishSaved = dishRepository.save(dish);

        assertThat(dishSaved).isNotNull();
    }
}