package com.popcorn.jrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JobRecruitmentPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobRecruitmentPlatformApplication.class, args);
    }

}
