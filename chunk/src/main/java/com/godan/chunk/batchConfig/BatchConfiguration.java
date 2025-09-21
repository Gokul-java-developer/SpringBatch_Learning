package com.godan.chunk.batchConfig;

import com.godan.chunk.reader.MyItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public BatchConfiguration(JobRepository jobRepository,
                              PlatformTransactionManager platformTransactionManager){
        this.platformTransactionManager = platformTransactionManager;
        this.jobRepository = jobRepository;
    }

    @Bean
    public ItemReader<String> itemReader(){
        List<String> productList = new ArrayList<>();
        productList.add("Product 1");
        productList.add("Product 2");
        productList.add("Product 3");
        productList.add("Product 4");
        productList.add("Product 5");
        productList.add("Product 6");
        productList.add("Product 7");
        productList.add("Product 8");
        return new MyItemReader(productList);
    }

    @Bean
    public Step step1(){
        return new StepBuilder("chunkStep-1", jobRepository)
                .<String, String>chunk(3,platformTransactionManager)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> chunk) throws Exception {
                        System.out.println("Writer Started");
                        chunk.forEach(System.out::println);
                        System.out.println("Writer Completed");
                    }
                }).build();
    }

    @Bean
    public Job firstJob(){
        return new JobBuilder("Job-1",jobRepository).start(step1()).build();
    }

}
