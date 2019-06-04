package demo.neuralrnn.repository;

import demo.neuralrnn.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product findByCusip(String cusip);
}
