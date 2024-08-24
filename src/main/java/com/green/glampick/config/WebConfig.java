package com.green.glampick.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String uploadPath;

    public WebConfig(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  // url 을 간단하게 함
        registry.addResourceHandler("/pic/**")  // /pic/ 다음에 요청이 들어온다면
                .addResourceLocations("file:" + uploadPath);  // 여기로 연결해줌
        // http://localhost:8080/pic/user/(유저pk)/(파일명)

        registry.addResourceHandler("/**")              // pic에 걸리지 않은 모든 요청
                .addResourceLocations("classpath:/static/**")       // 리소스 폴더 밑에있는 스태틱
                .resourceChain(true)                // 일단 내가 커스터마이징한 resolver 를 쓰고 안되면 스프링꺼를 쓰겟다
                .addResolver(new PathResourceResolver() {
                    @Override                                      // 커스터마이징한 resolver
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);

                        if(requestedResource.exists() && requestedResource.isReadable()) {  // 존재하고 읽을 수 있냐
                            return requestedResource;
                        }
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("*");
        }
}