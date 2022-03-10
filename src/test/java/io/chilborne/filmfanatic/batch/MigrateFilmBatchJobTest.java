package io.chilborne.filmfanatic.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.batch.operations.BatchRuntimeException;


@SpringBootTest
@ActiveProfiles("test")
class MigrateFilmBatchJobTest {

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  Job processJob;

  @Test
  public void executorTest() throws JobExecutionException, BatchRuntimeException {
    JobParameters jobParameters = new JobParametersBuilder()
      .addLong("time", System.currentTimeMillis())
      .toJobParameters();
    jobLauncher.run(processJob, jobParameters);
  }

}