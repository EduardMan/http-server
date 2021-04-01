package tech.itpark.http.configuration;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.itpark.http.FrontController;
import tech.itpark.http.Server;
import tech.itpark.http.bpp.RequestMappingBeanPostProcessor;

@Configuration
public class JavaConfig {
    @Bean
    public RequestMappingBeanPostProcessor getBPP(FrontController frontController) {
        return new RequestMappingBeanPostProcessor(frontController);
    }

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
