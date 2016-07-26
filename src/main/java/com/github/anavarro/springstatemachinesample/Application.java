package com.github.anavarro.springstatemachinesample;

import org.springframework.context.annotation.Import;
import com.github.anavarro.springstatemachinesample.workflow.WorkflowConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by anavarro on 23/07/16.
 */
@EnableAutoConfiguration
@Import( {WorkflowConfiguration.class})
public class Application {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}