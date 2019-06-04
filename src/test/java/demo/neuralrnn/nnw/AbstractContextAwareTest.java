package demo.neuralrnn.nnw;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public abstract class AbstractContextAwareTest implements ContextAwareTest {
    protected ApplicationContext applicationContext;

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        afterApplicationContext();
    }
}
