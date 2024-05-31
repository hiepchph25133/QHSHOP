package com.poly.configs;

import com.poly.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("H:\\Do an tot nghiep\\ban hang jsp\\New folder\\New folder\\fot\\fot\\testcuoi\\bills")
    private String externalDirectoryPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đường dẫn thư mục ngoài resource cho phép truy cập
        String rootPath = "file:"+externalDirectoryPath;

        registry.addResourceHandler("/external/**")
                .addResourceLocations(rootPath);
    }
}
