package hexagonal.modular.infrastructure.configs;

import hexagonal.modular.shared.exceptions.CustomAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final CustomAsyncExceptionHandler customAsyncExceptionHandler;

    public AsyncConfig(CustomAsyncExceptionHandler customAsyncExceptionHandler) {
        this.customAsyncExceptionHandler = customAsyncExceptionHandler;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return customAsyncExceptionHandler;
    }

    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        return new ThreadPoolTaskExecutor() {
            {
                setCorePoolSize(10);
                setMaxPoolSize(20);
                setQueueCapacity(100);
                setThreadNamePrefix("async-event-");
                setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                setWaitForTasksToCompleteOnShutdown(true);
                setAwaitTerminationSeconds(60);
                initialize();
            }
        };
    }
}
