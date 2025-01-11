package com.trade.repository;

import com.trade.modal.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
     Watchlist findByUserId(Long userId);
}
