package kr.co.lunasoft.lunaadmin.config;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author 최용석
 * @since 2019-05-28
 * {@link #https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api} 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("kr.co.lunasoft.lunaadmin.controller")) 	// 지정한 특정 pacakge 경로만 apidocs로 추출
//				.paths(PathSelectors.regex("^(/admin).+")) 		// 포함 경로
				.build()							
				.apiInfo(this.apiInfo());															// swagger main 화면 info customizing 
	}
	
	private ApiInfo apiInfo() {
		 return new ApiInfoBuilder()
	                .title("LunaSoft Admin API")
	                .description("루나소프트 Admin API \r\n" +
	                " + 통합어드민 API \r\n" +
	                "업데이트 일시 : " + new Date()
	                )
	                .termsOfServiceUrl("http://www.naver.com")
	                .contact(new Contact("루나소프트-코어개발팀", "https://git.lunasoft.co.kr/", "yschoi@lunasoft.co.kr"))
	                .license("루나소프트")
	                .licenseUrl("http://www.lunasoft.co.kr")
	                .version("0.01")
	                .build();
	}
}
