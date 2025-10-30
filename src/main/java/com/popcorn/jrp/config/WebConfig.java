package com.popcorn.jrp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path.company-images}")
    private String COMPANY_UPLOAD_DIR;
    @Value("${upload.path.candidate-images}")
    private String CANDIDATE_UPLOAD_DIR;
    @Value("${upload.path.resumes}")
    private String RESUME_UPLOAD_DIR;


    // Pageable config
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePagination() {
        return resolver -> {
            resolver.setMaxPageSize(50);            // Giới hạn kích thước tối đa
            resolver.setFallbackPageable(PageRequest.of(0, 10)); // Mặc định page=1,size=10
            resolver.setPageParameterName("page");  // Tên query param cho page
            resolver.setSizeParameterName("size");  // Tên query param cho size
            resolver.setPrefix("");
            resolver.setQualifierDelimiter("_");
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đường dẫn public client có thể truy cập
        registry.addResourceHandler("/images/companies/**")
                .addResourceLocations("file:"+COMPANY_UPLOAD_DIR);
        registry.addResourceHandler("/resumes/**")
                .addResourceLocations("file:"+RESUME_UPLOAD_DIR);
        registry.addResourceHandler("/images/candidates/**")
                .addResourceLocations("file:"+ CANDIDATE_UPLOAD_DIR);
    }
}
