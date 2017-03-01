package com.compica;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Tic> reader(){
		FlatFileItemReader<Tic> reader = new FlatFileItemReader<Tic>();
		ClassPathResource classPathResource = new ClassPathResource("DAT_MT_EURUSD_M1_2016.csv");
		reader.setResource(classPathResource);
		reader.setLineMapper(new DefaultLineMapper<Tic>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "date", "time", "open","high","low","close","volume"});
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Tic>() {
					{
						setTargetType(Tic.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public TicItemProcessor processor() {
		System.out.println("Bean item processor");
		return new TicItemProcessor();
	}

	@Bean
	public FlatFileItemWriter<Bar> writer() {
		FlatFileItemWriter<Bar> writer = new FlatFileItemWriter<Bar>();
		
		try {
			String barsOutputPath = File.createTempFile("barOutput", ".csv").getAbsolutePath();
			System.out.println(barsOutputPath);
			writer.setResource(new FileSystemResource(barsOutputPath));
			LineAggregator<Bar> lineAggregator=new DelimitedLineAggregator<Bar>();
			writer.setLineAggregator(lineAggregator);
			writer.afterPropertiesSet();		
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		return writer;
	}
	
    @Bean
    public Job job() {
        return jobBuilderFactory.get("ForexTimeSerie")
                .start(step1())
                .build();
    }
	
	@Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Tic, Bar> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
