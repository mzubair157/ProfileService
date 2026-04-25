package com.example.profile.service.service;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.domain.LoyaltyTransaction;
import com.example.profile.service.exception.InsufficientPointsException;
import com.example.profile.service.repository.LoyaltyRepository;
import com.example.profile.service.repository.LoyaltyTransactionRepository;
import com.example.profile.service.util.DataValidationUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyRepository repository;
    private final LoyaltyTransactionRepository transactionRepository;
    private final TransactionTemplate transactionTemplate;
    private final Executor loyaltyReconciliationExecutor;

    public LoyaltyServiceImpl(LoyaltyRepository repository,
                              LoyaltyTransactionRepository transactionRepository,
                              TransactionTemplate transactionTemplate,
                              @Qualifier("loyaltyReconciliationExecutor") Executor loyaltyReconciliationExecutor) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.transactionTemplate = transactionTemplate;
        this.loyaltyReconciliationExecutor = loyaltyReconciliationExecutor;
    }

    @Override
    @CacheEvict(value = "loyalty", key = "#accountId")
    public void adjustPoints(Long accountId, Long delta, String idempotencyKey) {
        DataValidationUtils.requirePositiveId(accountId, "accountId");
        DataValidationUtils.requireNonBlank(idempotencyKey, "idempotencyKey");

        CompletableFuture
                .supplyAsync(() -> reconcilePoints(accountId, delta, idempotencyKey), loyaltyReconciliationExecutor)
                .join();
    }

    private LoyaltyTransaction reconcilePoints(Long accountId, Long delta, String idempotencyKey) {
        return transactionRepository.findByAccountIdAndIdempotencyKey(accountId, idempotencyKey)
                .orElseGet(() -> createTransaction(accountId, delta, idempotencyKey));
    }

    private LoyaltyTransaction createTransaction(Long accountId, Long delta, String idempotencyKey) {
        try {
            return transactionTemplate.execute(status -> {
                LoyaltyTransaction existing = transactionRepository
                        .findByAccountIdAndIdempotencyKey(accountId, idempotencyKey)
                        .orElse(null);
                if (existing != null) {
                    return existing;
                }

                LoyaltyAccount account = repository.findById(accountId).orElseThrow();
                long newBalance = account.getBalance() + delta;
                if (newBalance < 0) {
                    throw new InsufficientPointsException();
                }

                account.setBalance(newBalance);
                LoyaltyTransaction transaction = new LoyaltyTransaction();
                transaction.setDelta(delta);
                transaction.setIdempotencyKey(idempotencyKey);
                transaction.setBalanceAfter(newBalance);
                account.recordTransaction(transaction);
                repository.save(account);
                return transaction;
            });
        } catch (DataIntegrityViolationException ex) {
            return transactionRepository.findByAccountIdAndIdempotencyKey(accountId, idempotencyKey)
                    .orElseThrow(() -> ex);
        }
    }
}
