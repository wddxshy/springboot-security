package org.shy.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.shy.beans.StateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/5 10:27
 * @Description
 */
@RestController
@Slf4j
public class VerifyController {

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @GetMapping("/verifyImg")
    public StateResult getVerifyImg(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String text = kaptchaProducer.createText();    //创建验证码文字
            log.info("验证码 --> {}", text);

            BufferedImage image = kaptchaProducer.createImage(text);     //通过文字创建验证码图片

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            String base64String = Base64.encodeBase64String(outputStream.toByteArray());
            String base64Image = "data:image/jpeg;base64," + base64String;

            String verifyKey = UUID.randomUUID().toString();
            log.info(verifyKey);
            strRedisTemplate.opsForValue().set(verifyKey,text);
            //req.getSession().setAttribute("verifyKey",verifyKey);

            return new StateResult(200, "verifyImage", base64Image);
        } catch (IOException e) {
            e.printStackTrace();
            return new StateResult(400, "获取验证码异常");
        }
    }
}
