package demo.neuralrnn.rule;

public interface Rule<R, T> {
    R apply(T t);

    void and(Rule<R, T> rule);

    void or(Rule<R, T> rule);
}
