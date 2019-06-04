package demo.neuralrnn.repository;

import demo.neuralrnn.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
