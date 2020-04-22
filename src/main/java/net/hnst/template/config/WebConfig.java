package net.hnst.template.config;

import net.hnst.template.inteceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private LoginInterceptor loginInterceptor;


    public WebConfig() {
        super();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //js - css - img - fonts 文件添加30天缓存
        registry.addResourceHandler("/vue/static/**")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/vue/static/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));

        //document文件不允许缓存
        registry.addResourceHandler("/vue/*")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/vue/")
                .setCacheControl(CacheControl.noStore());
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*") //可以跨域访问的URL路规则
                .allowedOrigins("*")  //可以跨域访问的访问者
                .allowedMethods("*") //可以跨域访问的方法
                .allowedHeaders("*")     //可以跨域访问的Headers
        ;
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/vue/index.html");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/").excludePathPatterns("/appliactionName")
                .excludePathPatterns("/vue/**");

//        registry.addInterceptor(templateInterceptor).addPathPatterns("/**");
//                .excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.ico", "/**/*.jpeg");

    }


}
