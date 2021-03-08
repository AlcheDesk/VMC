package com.meowlomo.vmc.config;

import static com.google.common.base.Predicates.or;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.google.common.base.Predicate;
 
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
/**
 * api doc -- springfox swagger configuration
 */
 
@Configuration
@EnableSwagger2
public class SwaggerConfig {
 
	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class); 
	
    @Value("${spring.jersey.application-path}")
    private String apiPath;
 
    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("access_token", "accessToken", "header");
    }
 
    @Bean
    public Docket apiConfig() {
    	logger.info("adding swagger for spring mvc controller");
        return new Docket(DocumentationType.SWAGGER_2).groupName("controller")
                // 调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
                .apiInfo(apiInfo()).select()
                // 控制暴露出去的路径下的实例
                // 如果某个接口不想暴露,可以使用以下注解
                // @ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
                .apis(RequestHandlerSelectors.basePackage("com.meowlomo.ems.api.jersey.resource")).paths(PathSelectors.any())
                .build().useDefaultResponseMessages(false).securitySchemes(Arrays.asList(apiKey()));
    }
 
    @Bean
    public Docket restConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("jax-rs").apiInfo(restInfo()).forCodeGeneration(true)
                .pathMapping("/").select().paths(paths())// 过滤的接口
                .build().useDefaultResponseMessages(false);
    }
 
    // 请求url匹配，支持and or，可以过滤筛选
    private Predicate<String> paths() {
        return or(regex("/test/.*"), regex("/rest/.*")); //
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Execution Management System")// 大标题
                .description("EMS API")// 小标题
                // .termsOfServiceUrl("http://ww.swagger.com/")
                // .contact(new Contact("swagger", "www.swagger.com",
                // "swagger@foxmail.com"))
                .version("2.0").build();
    }
 
    private ApiInfo restInfo() {
        return new ApiInfoBuilder().title("Execution Management System")// 大标题
                .description("EMS API")// 小标题
                .version("1.0").build();
    }
}
