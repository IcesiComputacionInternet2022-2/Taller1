package co.edu.icesi.zoo.repository;

import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.model.Animal;
import org.junit.jupiter.api.Test;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    public void whenCalledSave_thenCorrectNumberOfUsers() {
        animalRepository.save(Animal.builder().name("Lupe").sex(AnimalGender.M).age(10).height(12).weight(150).arrivalDate(LocalDateTime.of(2022, 9, 8, 0, 0, 0)).build());
        List<Animal> a = (List<Animal>) animalRepository.findAll();

        //Get 3 from initial data config
        assertThat(a.size()).isEqualTo(4);
    }
}
