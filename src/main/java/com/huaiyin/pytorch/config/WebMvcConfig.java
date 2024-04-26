package com.huaiyin.pytorch.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.huaiyin.pytorch.common.JacksonObjectMapper;
import com.huaiyin.pytorch.utils.LoginIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * ClassName:WebMvcConfig
 * Package:com.huaiyin.pytorch.common
 * Description:
 * web应用设置
 *
 * @Author 卜翔威
 * @Create 2024/4/26 19:40
 * @Version 1.0
 */
@EnableKnife4j
@EnableSwagger2
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 登录拦截器
		registry.addInterceptor(new LoginIntercepter()).
				excludePathPatterns(
						"/user/register",
						"/user/login",
						"/user/logout",
						"/v2/api-docs",
						"/swagger-resources/configuration/ui",
						"/swagger-resources",
						"/swagger-resources/configuration/security",
						"/webjars/**",
						"/swagger-ui.html",
						"/doc.html",
						"/favicon.ico"
						);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("开始静态资源映射");
		// 静态资源映射
		//swagger文件的静态资源映射
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

		log.info("扩展消息转换器...");
		// 创建消息转换器对象
		MappingJackson2HttpMessageConverter
				HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		// 设置对象转换器，底层使用jackson将java对象转换成json
		HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());

		// 将上面的消息转换器对象追加到mvc框架的转换器集合中,0表示将自己定义的转换器放在前面
		converters.add(0, HttpMessageConverter);
		super.extendMessageConverters(converters);
	}

	// swagger文档生成器的配置
	@Bean
	public Docket createRestApi() {
		// 文档类型
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.huaiyin.pytorch.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("pytorch系统")
				.version("1.0")
				.description("pytorch接口文档")
				.build();
	}
}
