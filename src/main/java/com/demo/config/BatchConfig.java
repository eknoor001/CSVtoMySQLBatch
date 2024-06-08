package com.demo.config;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.demo.model.Loan;

@Configuration
@EnableBatchProcessing
@ComponentScan("com.demo.config")
public class BatchConfig {

    @Autowired
    public DataSource dataSource;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean
    public FlatFileItemReader<Loan> reader() {
        FlatFileItemReader<Loan> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("Loan_data.csv"));
        reader.setLineMapper(getLineMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public LineMapper<Loan> getLineMapper() {
        DefaultLineMapper<Loan> lineMapper = new DefaultLineMapper<Loan>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("Loan_ID", "loan_status", "Principal",
                "terms", "effective_date", "due_date", "paid_off_time",
                "past_due_days", "age", "education", "Gender");
        lineTokenizer.setIncludedFields(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        BeanWrapperFieldSetMapper<Loan> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Loan.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        fieldSetMapper.setCustomEditors(Collections.singletonMap(Date.class, new CustomDateEditor(dateFormat, true)));

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public UserItemProcessor Processor() {
        return new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Loan> writer() {
        JdbcBatchItemWriter<Loan> writer = new JdbcBatchItemWriter<Loan>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("insert into loan(loan_id,status,principal,terms,effective_date,due_date,paid_off_time,past_due_days,age,education,gender)"
                + " values(:loan_id,:status,:principal,:terms,:effective_date,:due_date,:paid_off_time,:past_due_days,:age,:education,:gender)");
        writer.setDataSource(this.dataSource);
        return writer;
    }

    @Bean
    public Job importLoanJob() {
        return this.jobBuilderFactory.get("USER-IMPORT-JOB").incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {

        return this.stepBuilderFactory.get("step1")
                .<Loan, Loan>chunk(10)
                .reader(reader())
                .processor(Processor())
                .writer(writer())
                .build();

    }

}

