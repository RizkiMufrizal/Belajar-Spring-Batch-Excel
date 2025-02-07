package org.rizki.mufrizal.belajar.spring.batch.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.rizki.mufrizal.belajar.spring.batch.excel.object.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean()
    public Job downloadCsvFileJob() {
        return new JobBuilder("downloadCsvFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(downloadCsvFileStep())
                .build();
    }

    @Bean
    public Step downloadCsvFileStep() {
        return new StepBuilder("downloadCsvFileStep", jobRepository)
                .<Employee, Employee>chunk(100, transactionManager)
                .reader(excelItemReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @StepScope
    @Bean
    public PoiItemReader<Employee> excelItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        log.info("Reading excel file {}", filePath);
        var reader = new PoiItemReader<Employee>();
        reader.setLinesToSkip(1);
        reader.setResource(new PathResource(filePath));
        reader.setRowMapper(new BeanWrapperRowMapper<>() {
            {
                setTargetType(Employee.class);
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Employee, Employee> processor() {
        return (employee) -> {
            log.info("Processing excel file");
            return employee;
        };
    }

    @Bean
    public ItemWriter<Employee> writer() {
        return items -> log.info("Writing employees {}", new ObjectMapper().writeValueAsString(items));
    }
}