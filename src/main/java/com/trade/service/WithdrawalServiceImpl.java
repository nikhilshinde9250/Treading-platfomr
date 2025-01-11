package com.trade.service;

import com.trade.domain.WithdrawalStatus;
import com.trade.modal.User;
import com.trade.modal.Withdrawal;
import com.trade.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService{

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal=new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proccedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawal=withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isEmpty()){
            throw new Exception("withdrawal not found ");
        }
        Withdrawal withdrawal1=withdrawal.get();
        withdrawal1.setDate(LocalDateTime.now());
        if (accept==true){
            withdrawal1.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
        }else {
            withdrawal1.setWithdrawalStatus(WithdrawalStatus.DECLINE);
        }
        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
