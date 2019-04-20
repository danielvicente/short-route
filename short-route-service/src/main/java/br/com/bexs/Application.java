package br.com.bexs;

import br.com.bexs.domain.service.RouteServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.input.file.location}")
    private String inputFileLocation;

    @Override
    public void run(String... args) throws Exception {
        if (ObjectUtils.isEmpty(args) && inputFileLocation == null) {
            log.warn("No file found. Please provide an file using command '-Dspring-boot.run.arguments={fileLocation}'.");
            throw new RuntimeException("No file found. Please provide an file using command '-Dspring-boot.run.arguments={fileLocation}'.");
        }

        String fileLocation = ObjectUtils.isEmpty(args) ? this.getClass().getClassLoader().getResource(inputFileLocation).getPath() : args[0];
        log.info("Initialized with file located in '{}'.", fileLocation);
        this.routeServiceFacade.setFileLocation(fileLocation);
        this.routeServiceFacade.initRoutes();
    }
}