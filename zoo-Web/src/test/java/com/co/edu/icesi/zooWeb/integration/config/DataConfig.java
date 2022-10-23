package com.co.edu.icesi.zooWeb.integration.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

public class DataConfig {
    @Autowired
    public void configureInitialData(DataSource dataSource, SpringLiquibase liquibase) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/data.sql"));
        DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
    }
}
