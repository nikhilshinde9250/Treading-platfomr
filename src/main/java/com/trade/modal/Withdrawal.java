package com.trade.modal;

import com.trade.domain.WithdrawalStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus withdrawalStatus;

    @ManyToOne
    private User user;

    private LocalDateTime date=LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    private Long amount;



}
