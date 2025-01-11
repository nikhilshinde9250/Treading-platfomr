package com.trade.service;

import com.trade.modal.Coin;
import com.trade.modal.User;
import com.trade.modal.Watchlist;
import com.trade.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist findUserByWatchlist(Long userId) throws Exception {
        Watchlist watchlist=watchlistRepository.findByUserId(userId);
        if (watchlist==null){
            throw new Exception("watchlist not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {
        Watchlist watchlist=new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchlistOptional=watchlistRepository.findById(id);
        if (watchlistOptional.isEmpty()){
            throw new Exception("watchlist not found");
        }
        return watchlistOptional.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist=findUserByWatchlist(user.getId());
        if (watchlist.getCoins().contains(coin)){
            watchlist.getCoins().remove(coin);
        }
        else {
            watchlist.getCoins().add(coin);
        }
        watchlistRepository.save(watchlist);
        return coin;
    }
}
