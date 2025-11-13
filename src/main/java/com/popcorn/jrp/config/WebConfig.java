package com.popcorn.jrp.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

        @Value("${upload.path.companies}")
        private String COMPANY_UPLOAD_DIR;
        @Value("${upload.path.candidates}")
        private String CANDIDATE_UPLOAD_DIR;
        @Value("${upload.path.resumes}")
        private String RESUME_UPLOAD_DIR;
        @Value("${upload.path.applications}")
        private String APPLICATION_UPLOAD_DIR;

        // Pageable config
        // @Bean
        // public PageableHandlerMethodArgumentResolverCustomizer customizePagination()
        // {
        // return resolver -> {
        // resolver.setOneIndexedParameters(true);
        // resolver.setMaxPageSize(50); // Giới hạn kích thước tối đa
        // resolver.setFallbackPageable(PageRequest.of(0, 10)); // Mặc định
        // page=1,size=10
        // resolver.setPageParameterName("page"); // Tên query param cho page
        // resolver.setSizeParameterName("size"); // Tên query param cho size
        // resolver.setPrefix("");
        // resolver.setQualifierDelimiter("_");
        // };
        // }

        // Pageable config and Format param sort
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                // 🔹 Custom Sort Resolver
                SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
                sortResolver.setSortParameter("sort");
                sortResolver.setPropertyDelimiter("_"); // asc:name

                // 🔹 Custom Pageable Resolver
                PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(
                                sortResolver);
                pageableResolver.setOneIndexedParameters(true);
                pageableResolver.setMaxPageSize(50);
                pageableResolver.setFallbackPageable(PageRequest.of(0, 10));

                // 🔹 Đổi tên các tham số page/size
                pageableResolver.setPageParameterName("page"); // mặc định là "page"
                pageableResolver.setSizeParameterName("size"); // mặc định là "size"

                resolvers.add(pageableResolver);
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Đường dẫn public client có thể truy cập
                registry.addResourceHandler("/images/companies/**")
                                .addResourceLocations("file:" + COMPANY_UPLOAD_DIR);
                registry.addResourceHandler("/images/resumes/**")
                                .addResourceLocations("file:" + RESUME_UPLOAD_DIR);
                registry.addResourceHandler("/images/candidates/**")
                                .addResourceLocations("file:" + CANDIDATE_UPLOAD_DIR);
                registry.addResourceHandler("/images/applications/**")
                                .addResourceLocations("file:" + APPLICATION_UPLOAD_DIR);
        }
}
