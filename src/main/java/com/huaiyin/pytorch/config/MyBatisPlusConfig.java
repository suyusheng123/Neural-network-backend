package com.huaiyin.pytorch.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:MyBatisPlusConfig
 * Package:com.huaiyin.pytorch.config
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/5/9 10:33
 * @Version 1.0
 */
@Configuration
public class MyBatisPlusConfig {
	@Bean//使用在方法上，标注将该方法的返回值存储到Spring容器中
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return mybatisPlusInterceptor;
	}
}

