package net.hnst.template.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//这里对应 myprops 下的属性
@Component
@ConfigurationProperties(prefix = "myprops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyProperties {

    private String applicationName;
}
