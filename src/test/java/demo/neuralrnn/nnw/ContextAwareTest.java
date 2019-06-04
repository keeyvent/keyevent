package demo.neuralrnn.nnw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public interface ContextAwareTest extends ApplicationContextAware {
    ApplicationContext getApplicationContext();
    void afterApplicationContext();
}
