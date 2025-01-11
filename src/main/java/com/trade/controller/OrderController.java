package com.trade.controller;

import com.trade.domain.OrderType;
import com.trade.modal.Coin;
import com.trade.modal.Order;
import com.trade.modal.User;
import com.trade.request.CreateOrderRequest;
import com.trade.service.CoinService;
import com.trade.service.OrderService;
import com.trade.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;


    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    ) throws  Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(req.getCoinId());

        Order order=orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Order order=orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }else {
            throw new Exception("you don't have access user");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    )throws Exception{
        Long userId=userService.findUserProfileByJwt(jwt).getId();
        List<Order> userOrders=orderService.getAllOrderOfUser(userId, String.valueOf(order_type),asset_symbol);
        return ResponseEntity.ok(userOrders);
    }

}
