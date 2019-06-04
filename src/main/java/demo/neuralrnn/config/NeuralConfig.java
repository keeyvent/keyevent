package demo.neuralrnn.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(value="demo.neuralrnn")
@EnableJpaRepositories(basePackages = "demo.neuralrnn.repository")
public class NeuralConfig {

}
