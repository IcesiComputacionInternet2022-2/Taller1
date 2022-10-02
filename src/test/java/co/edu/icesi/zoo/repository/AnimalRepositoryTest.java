package co.edu.icesi.zoo.repository;

import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.model.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    public void whenCalledSave_thenCorrectNumberOfUsers() {
        animalRepository.save(Animal.builder().name("Luna").sex(AnimalGender.M).age(10).height(12).weight(150).arrivalDate(LocalDateTime.of(2022, 9, 8, 0, 0, 0)).build());
        List<Animal> a = (List<Animal>) animalRepository.findAll();

        assertThat(a.size()).isEqualTo(1);
    }
}
