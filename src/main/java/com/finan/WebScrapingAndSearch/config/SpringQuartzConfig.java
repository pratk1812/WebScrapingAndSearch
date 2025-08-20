package com.finan.WebScrapingAndSearch.config;

import javax.sql.DataSource;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableAutoConfiguration
public class SpringQuartzConfig {

  private final ApplicationContext applicationContext;
  private final DataSource dataSource;

  @Autowired
  public SpringQuartzConfig(ApplicationContext applicationContext, DataSource dataSource) {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
  }

  @Bean
  public JobFactory springBeanJobFactory() {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean scheduler(Trigger... triggers) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

    schedulerFactory.setOverwriteExistingJobs(true);
    schedulerFactory.setAutoStartup(true);
    schedulerFactory.setJobFactory(springBeanJobFactory());
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
    schedulerFactory.setDataSource(dataSource);

    if (triggers != null) {
      schedulerFactory.setTriggers(triggers);
    }

    return schedulerFactory;
  }
}
