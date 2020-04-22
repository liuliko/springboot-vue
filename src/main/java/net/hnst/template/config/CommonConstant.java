package net.hnst.template.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommonConstant implements ApplicationContextAware {


    private static ApplicationContext context;

    //获取当前系统启用的 配置
    public String getActiveProfile() {
        String[] profiles = context.getEnvironment().getActiveProfiles();
        if (profiles.length != 0) {
            return profiles[0];
        }
        return "";
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
