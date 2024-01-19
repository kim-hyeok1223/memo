package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;

@Configuration // ������ ���� springbean
public class WebMvcConfig implements WebMvcConfigurer{

	// �� �̹��� path�� ������ ���ε� �� ���� �̹����� ���� ����
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/images/**") // web path   http://localhost:/images/aaaa_1234123421/sun.png
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // ���� �̹��� ���� ��ġ
	}
}
