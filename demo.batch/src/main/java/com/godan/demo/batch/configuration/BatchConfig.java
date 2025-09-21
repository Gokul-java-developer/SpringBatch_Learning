package com.godan.demo.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.godan.demo.batch.lisitener.MyStepExecutionListener;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public StepExecutionListener myStepExecutionListener(){
        return new MyStepExecutionListener();
    }


    @Bean
    public Step step1(){
        return new StepBuilder("step-1",jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Step-1 Completed");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }

    @Bean
    public Step step2(){

        boolean isTrue = true;
        return new StepBuilder("step-2",jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                if(isTrue){
                    throw new Exception("My Test Exception");
                }
                System.out.println("Step-2 Completed");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).listener(myStepExecutionListener()).build();
    }

    @Bean
    public Step step3(){
        return new StepBuilder("step-3",jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Step-3 Completed");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }

    @Bean
    public Step step4(){
        return new StepBuilder("step-4",jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Step-4 Completed");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }





    @Bean("firstJob")
    public Job job(){
        return  new JobBuilder("job-1", jobRepository)
                .start(step1())
                    .on("COMPLETED").to(step2())
                .from(step2())
                    .on("TEST_STATUS").to(step4())
                .from(step2())
                    .on("*").to(step3())
                .end()
                .build();
    }
}
