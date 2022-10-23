package co.edu.icesi.zoo.repository;

import co.edu.icesi.zoo.model.Tatabro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration()
@DataJpaTest
public class TatabroRepositoryTest {

    @Autowired
    private TatabroRepository userRepository;

    @Test
    public void whenCalledSave_thenCorrectNumberOfTatabros() {
        userRepository.save(Tatabro.builder().id(UUID.fromString("2f7908f1-c6c5-4fd9-bca9-07332d2c60e6")).name("Sammy Pig").sex("M").weight(20.0).age(15).height(50.0).arrivalDate(LocalDateTime.parse("2021-01-16T10:30:25")).fatherID(null).motherID(null).build());
        List<Tatabro> tatabros = (List<Tatabro>) userRepository.findAll();
        assertThat(tatabros.size()).isEqualTo(1);
    }
}