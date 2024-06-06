package dev.mutti.simplepaymentsapi.services;

import dev.mutti.simplepaymentsapi.domain.Transaction;
import dev.mutti.simplepaymentsapi.domain.User;
import dev.mutti.simplepaymentsapi.exception.TransactionNotAuthorizedException;
import dev.mutti.simplepaymentsapi.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    /**
     * Find all transactions
     *
     * @return List of transactions
     */
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Create a transaction
     *
     * @param transaction Transaction to be created
     * @return String message
     * @throws IllegalArgumentException          if payer and payee are the same / if payer is a merchant / if payer balance is insufficient
     * @throws TransactionNotAuthorizedException if transaction is not authorized
     */
    @Transactional
    public String createTransaction(Transaction transaction) {
        validatePayerAndPayeeDifferent(transaction);
        validatePayerMerchant(transaction.getPayerId());
        var payer = userService.findById(transaction.getPayerId());
        isPayerBalanceSufficient(payer, transaction.getValue());

        if (!authorizeTransaction()) {
            throw new TransactionNotAuthorizedException("Transaction not authorized");
        }

        updateBalances(transaction);
        transactionRepository.save(transaction);

        return "Transaction was successful";
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

    private boolean authorizeTransaction() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://util.devi.tools/api/v2/authorize",
                    HttpMethod.GET,
                    null,
                    String.class);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            log.info("Error while authorizing transaction: {}", e.getMessage());
        }

        return false;
    }
}
