package com.github.anavarro.springstatemachinesample;

import com.github.anavarro.springstatemachinesample.workflow.SimplifiedWorkflowConfiguration;
import com.github.anavarro.springstatemachinesample.workflow.WorkflowConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by anavarro on 23/07/16.
 */
@EnableAutoConfiguration
@Import( {SimplifiedWorkflowConfiguration.class})
public class Application {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}