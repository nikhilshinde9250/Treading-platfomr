package com.trade.service;

import com.trade.modal.User;
import com.trade.modal.Withdrawal;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithdrawal(Long amount, User user);

    Withdrawal proccedWithdrawal(Long withdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();
}
