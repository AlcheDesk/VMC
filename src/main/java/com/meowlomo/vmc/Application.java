package com.meowlomo.vmc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import com.meowlomo.vmc.util.RestartRebootUtils;

@SpringBootApplication(
    scanBasePackages = {
        //main configuration classes
        "com.meowlomo.vmc.config", 
        //scheduler
        "com.meowlomo.vmc.scheduler",
        "com.meowlomo.vmc.event",
//        "com.meowlomo.vmc.event.handler",
        //listener classes
        "com.meowlomo.vmc.listener",
        //jersey REST classes
        "com.meowlomo.vmc.jersey",
        "com.meowlomo.vmc.util",
        //system properties
        "com.meowlomo.vmc.core",
        "com.meowlomo.vmc.task",
        "com.meowlomo.vmc.file",
        "com.meowlomo.vmc.service"
    }
)
@EnableCaching
//@Import(value={SpringUtils.class})
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO i cann't see the necessity for rebooting here, should delete the reboot call if you see this at year 2020. By Andrew
		RestartRebootUtils.restart();
		return builder.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}