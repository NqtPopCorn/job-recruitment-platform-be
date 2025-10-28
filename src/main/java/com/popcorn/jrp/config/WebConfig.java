package com.popcorn.jrp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    // Pageable config
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePagination() {
        return resolver -> {
            resolver.setOneIndexedParameters(true);
            resolver.setMaxPageSize(50);            // Giới hạn kích thước tối đa
            resolver.setFallbackPageable(PageRequest.of(0, 10)); // Mặc định page=1,size=10
            resolver.setPageParameterName("page");  // Tên query param cho page
            resolver.setSizeParameterName("size");  // Tên query param cho size
            resolver.setPrefix("");
            resolver.setQualifierDelimiter("_");
        };
    }
}
