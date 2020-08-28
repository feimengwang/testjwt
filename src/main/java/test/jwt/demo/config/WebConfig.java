package test.jwt.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import test.jwt.demo.interceptor.DemoInterceptor;

import java.util.List;

/**
 * @author Java猿
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    DemoInterceptor demoInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(demoInterceptor);
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/login");
    }


    @Autowired
    public void setDemoInterceptor(DemoInterceptor demoInterceptor) {
        this.demoInterceptor = demoInterceptor;
    }
}
