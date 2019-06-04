package demo.neuralrnn.repository;

import demo.neuralrnn.entity.DefaultRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultRatingRepository extends JpaRepository<DefaultRating, Integer> {
}
