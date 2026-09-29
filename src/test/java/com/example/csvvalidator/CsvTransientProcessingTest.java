package com.example.csvvalidator;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class CsvTransientProcessingTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void applicationHasNoDatabaseDataSourceBean() {
        assertThat(applicationContext.getBeanProvider(DataSource.class).getIfAvailable()).isNull();
    }
}
