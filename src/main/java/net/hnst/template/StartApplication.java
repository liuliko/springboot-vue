package net.hnst.template;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableScheduling
@EnableAsync
public class StartApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(StartApplication.class, args);
//        SpringUtil.setApplicationContext(context);
    }

    /**
     * 实现SpringBootServletInitializer可以让spring-boot项目在web容器中运行
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        builder.sources(this.getClass());
//        SpringUtil.setApplicationContext(builder.context());
        return super.configure(builder);
    }


    /**
     * 解决springboot的文件临时目录被系统自动删除的问题
     * springboot启动时会创建一个/tmp/tomcat.7/work/Tomcat/localhost/ROOT的临时目录作为文件上传的临时目录，但是该目录会在10天之后被系统自动清理掉。
     *
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/app/shj/tmp");
        return factory.createMultipartConfig();
    }


    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
        }
    }




    static {
        //break JCE crypto policy limit
        //解除aes秘钥长度限制 (java限制128位)
        try {
            Class<?> clazz = Class.forName("javax.crypto.JceSecurity");
            Field nameField = clazz.getDeclaredField("isRestricted");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);
            nameField.setAccessible(true);
            nameField.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
