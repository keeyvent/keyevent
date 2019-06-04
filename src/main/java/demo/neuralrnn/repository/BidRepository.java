package demo.neuralrnn.repository;

import demo.neuralrnn.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("SELECT b FROM Bid b JOIN b.trade t JOIN b.client c WHERE t.tradeId=:tradeId and c.id=:clientId")
    public Bid findBidByTradeIdAndClientId(@Param("tradeId") String tradeId, @Param("clientId") Long clientId);

    @Query("SELECT b FROM Bid b JOIN b.trade t WHERE t.tradeId=:tradeId")
    public List<Bid> findBidByTradeId(@Param("tradeId") String tradeId);

    @Query("SELECT b FROM Bid b JOIN b.client c WHERE c.id=:clientId")
    public Bid findBidByClientId(@Param("clientId") Long clientId);

}
