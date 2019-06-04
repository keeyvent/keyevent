package demo.neuralrnn.data;

public interface DataGenerator<T, R> {
    R generate(T t);
}
