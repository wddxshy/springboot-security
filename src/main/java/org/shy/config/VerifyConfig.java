package org.shy.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 12:34
 * @Description   谷歌验证码配置类
 */
@Configuration
public class VerifyConfig {

    @Bean
    public Producer getProducer(){
        Properties verifyProp = new Properties();
        verifyProp.put("kaptcha.border", "no");
        verifyProp.put("kaptcha.textproducer.char.length","6");
        verifyProp.put("kaptcha.image.height","50");
        verifyProp.put("kaptcha.image.width","140");
        verifyProp.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        verifyProp.put("kaptcha.textproducer.font.color","black");
        verifyProp.put("kaptcha.textproducer.font.size","40");
        verifyProp.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        verifyProp.put("kaptcha.textproducer.char.string","abcdefghijklmnopqrstuvwxyz0123456789");

        Config config = new Config(verifyProp);
        return config.getProducerImpl();
    }

}
