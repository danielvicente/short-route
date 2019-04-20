package br.com.bexs;

import br.com.bexs.domain.service.RouteServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ObjectUtils;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    RouteServiceFacade routeServiceFacade;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (ObjectUtils.isEmpty(args)) {
            log.info("No file found.");
            throw new RuntimeException("No file found. Please provide an file using command.");
        }
        String fileLocation = args[0];
        log.info("Initialized with file located in '{}'.", fileLocation);
        this.routeServiceFacade.initRoutes(fileLocation);
    }
}