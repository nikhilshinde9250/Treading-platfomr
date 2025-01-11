package com.trade.service;

import com.trade.domain.OrderStatus;
import com.trade.domain.OrderType;
import com.trade.modal.*;
import com.trade.repository.OrderItemRepository;
import com.trade.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AssetService assetService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        BigDecimal price = orderItem.getCoin().getCurrentPrice().multiply(orderItem.getQuantity());

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(price);
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("order not found"));
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, String OrderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity,
                                      BigDecimal buyPrice, BigDecimal sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(BigDecimal.valueOf(quantity));
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("quantity should be >0");
        }
        BigDecimal buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, BigDecimal.valueOf(0));

        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);

        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        //create asseet
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
                order.getUser().getId(),
                order.getOrderItem().getCoin().getId());
        if (oldAsset == null) {
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity().doubleValue());
        } else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("quantity should be >0");
        }
        BigDecimal sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        double buyPrice = assetToSell.getBuyPrice();
        if (assetToSell != null) {
            OrderItem orderItem = createOrderItem(coin, quantity, BigDecimal.valueOf(buyPrice), sellPrice);

            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if (assetToSell.getQuantity().compareTo(BigDecimal.valueOf(quantity)) >= 0) {

                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity().multiply(coin.getCurrentPrice()).compareTo(BigDecimal.ONE) <= 0) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }
            throw new Exception("Insufficient quantity to sell");
        }
        throw new Exception("asset not found");
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {

        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin, quantity, user);
        }

        throw new Exception("invalid order type");
    }
}
