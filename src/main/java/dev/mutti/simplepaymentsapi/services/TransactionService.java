package dev.mutti.simplepaymentsapi.services;

import dev.mutti.simplepaymentsapi.domain.Transaction;
import dev.mutti.simplepaymentsapi.domain.User;
import dev.mutti.simplepaymentsapi.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    @Transactional
    public void createTransaction(Transaction transaction) {
        validatePayerAndPayeeDifferent(transaction);
        validatePayerMerchant(transaction.getPayerId());
        var payer = userService.findById(transaction.getPayerId());
        isPayerBalanceSufficient(payer, transaction.getValue());

        updateBalances(transaction);

        transactionRepository.save(transaction);
    }

    private void isPayerBalanceSufficient(User payer, double transactionValue) {
        if (payer.getBalance() < transactionValue) {
            throw new IllegalArgumentException("Payer balance is insufficient");
        }
    }

    private void validatePayerAndPayeeDifferent(Transaction transaction) {
        if ((transaction.getPayerId() == transaction.getPayeeId())) {
            throw new IllegalArgumentException("Payer and payee must be different");
        }
    }

    private void validatePayerMerchant(long payerId) {
        var payer = userService.findById(payerId);
        if (payer.getType().isMerchant()) {
            throw new IllegalArgumentException("Payer cannot be a merchant");
        }
    }

    private void updateBalances(Transaction transaction) {
        userService.updateBalance(transaction.getPayerId(), (transaction.getValue() * -1));
        userService.updateBalance(transaction.getPayeeId(), transaction.getValue());
    }
}
