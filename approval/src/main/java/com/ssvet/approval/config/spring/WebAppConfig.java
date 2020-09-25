package com.ssvet.approval.config.spring;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebAppConfig implements WebMvcConfigurer
{

    @Value("${upload.path}")
    private String mImagesPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        if (mImagesPath.equals("") || mImagesPath.equals("${upload.path}"))
        {
            String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
            if (imagesPath.indexOf(".jar") > 0)
            {
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            }
            else if (imagesPath.indexOf("classes") > 0)
            {
                imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/images/";
            mImagesPath = imagesPath;
        }
       log.info("imagesPath=" + mImagesPath);
        registry.addResourceHandler("/**").addResourceLocations(mImagesPath);
    }
}
