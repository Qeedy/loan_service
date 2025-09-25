package com.projectlos.loan_service.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.ManagementService;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FlowableConfig implements EngineConfigurationConfigurer<ProcessEngineConfigurationImpl> {
    
    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {
        log.info("Configuring Flowable Process Engine");
        processEngineConfiguration.setAsyncExecutorActivate(false);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
    }
    
    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }
    
    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
    
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }
    
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }
    
    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }
}
