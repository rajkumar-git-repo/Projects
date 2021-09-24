package com.mli;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Nikhilesh.Tiwari
 *
 *
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MLIApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MLIApplication.class, args);
	}
	
	@Bean(name = "sessionFactory")
    public SessionFactory sessionFactory(HibernateEntityManagerFactory factory) {
        return factory.getSessionFactory();
    }
	
    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }
}
