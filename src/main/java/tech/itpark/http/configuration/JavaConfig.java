package tech.itpark.http.configuration;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.itpark.http.FrontController;
import tech.itpark.http.Server;

@Configuration
public class JavaConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public FrontController getFrontController(Gson gson) {
        return new FrontController(gson);
    }

    @Bean
    public Server getServer() {
        return new Server();
    }
}
