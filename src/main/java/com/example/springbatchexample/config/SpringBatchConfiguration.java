package com.example.springbatchexample.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.springbatchexample.model.User;

@Configuration
public class SpringBatchConfiguration {
	
	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory,ItemReader<User> reader,ItemProcessor<User,User> processor,ItemWriter<User> writer) {
		
		
		Step step=stepBuilderFactory.get("ETL-step-payload")
				                    .<User,User>chunk(100)
				                    .reader(reader)
				                    .processor(processor)
				                    .writer(writer)
				                    .build();
		
		return jobBuilderFactory.get("ETL-Payload")
		                 .incrementer(new RunIdIncrementer())
		                 .start(step)
		                 .build();	
		}
		
	@Bean
	public FlatFileItemReader<User> flatFileItemReader (@Value("${input}") Resource resource){
		FlatFileItemReader<User> flatFileReader=new FlatFileItemReader<>();
		flatFileReader.setResource(resource);
		flatFileReader.setName("CSV-READER");
		flatFileReader.setLinesToSkip(1);
		flatFileReader.setLineMapper(lineMapper());
		return flatFileReader;
		

}

	private LineMapper<User> lineMapper() {
		
		DefaultLineMapper<User> defaultLineMapper=new DefaultLineMapper<User>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames(new String[] {"id","name","dept","salary"});
		
		/**
		 * this BeanWrapper is used to map the fields with pojo(model)
		 */
		BeanWrapperFieldSetMapper<User> fieldMapper=new BeanWrapperFieldSetMapper<>();
		
		fieldMapper.setTargetType(User.class);
		
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldMapper);
		
		
		return defaultLineMapper;
	}

}
