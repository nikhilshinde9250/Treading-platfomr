package com.trade.controller;

import com.trade.modal.Coin;
import com.trade.modal.User;
import com.trade.modal.Watchlist;
import com.trade.service.CoinService;
import com.trade.service.UserService;
import com.trade.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Watchlist watchlist=watchListService.findUserByWatchlist(user.getId());
        return ResponseEntity.ok(watchlist);
    }


    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(@PathVariable Long watchlistId) throws Exception {
        Watchlist watchlist=watchListService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Coin coin= coinService.findById(coinId);

        Coin addCoin=watchListService.addItemToWatchlist(coin, user);
        return ResponseEntity.ok(addCoin);
    }


}
