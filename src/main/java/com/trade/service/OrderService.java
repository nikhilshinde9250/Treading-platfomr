package com.trade.service;

import com.trade.domain.OrderType;
import com.trade.modal.Coin;
import com.trade.modal.Order;
import com.trade.modal.OrderItem;
import com.trade.modal.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;

    List<Order>getAllOrderOfUser(Long userId,String OrderType,String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

}
