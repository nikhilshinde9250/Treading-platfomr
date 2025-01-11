package com.trade.service;

import com.trade.modal.Coin;
import com.trade.modal.User;
import com.trade.modal.Watchlist;

public interface WatchListService {
    Watchlist findUserByWatchlist(Long userId) throws Exception;

    Watchlist createWatchlist(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;

}
