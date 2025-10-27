package com.popcorn.jrp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    // Pageable config
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePagination() {
        return resolver -> {
            resolver.setOneIndexedParameters(true); // Trang bắt đầu từ 1 thay vì 0
            resolver.setMaxPageSize(50);            // Giới hạn kích thước tối đa
            resolver.setFallbackPageable(org.springframework.data.domain.PageRequest.of(1, 10)); // Mặc định page=0,size=10
            resolver.setPageParameterName("page");  // Tên query param cho page
            resolver.setSizeParameterName("size");  // Tên query param cho size
            resolver.setPrefix("");
            resolver.setQualifierDelimiter("_");
        };
    }
}
