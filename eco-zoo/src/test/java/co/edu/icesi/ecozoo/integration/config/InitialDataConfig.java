package co.edu.icesi.ecozoo.integration.config;

import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeDeserializer;
import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Configuration
public class InitialDataConfig {

    @Autowired
    public void configureInitialData(DataSource dataSource, SpringLiquibase liquibase) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/DML.sql"));
        DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
    }
}
