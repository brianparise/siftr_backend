package com.siftr.concurrency;

import com.siftr.SftpClient.SftpClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ConcurrencyService {


   // @Qualifier("applicationTaskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private SftpClient sftpClient;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        this.taskExecutor.execute(sftpClient);
    }

}
