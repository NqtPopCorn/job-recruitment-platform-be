package com.popcorn.jrp.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
}
