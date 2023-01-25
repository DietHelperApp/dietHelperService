package app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class App {

    App() {
        log.info("APP INITIALISED area=dietHelper service=dietHelperService version={}",
                getClass().getPackage().getImplementationVersion());
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
