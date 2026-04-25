package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.domain.LoyaltyTransaction;
import com.example.profile.service.exception.InsufficientPointsException;
import com.example.profile.service.repository.LoyaltyRepository;
import com.example.profile.service.repository.LoyaltyTransactionRepository;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
class LoyaltyServiceTest {

    @Mock
    private LoyaltyRepository loyaltyRepository;

    @Mock
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Test
    void adjustPointsUpdatesBalanceAndAppendsTransaction() {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setId(5L);
        account.setBalance(100L);

        when(loyaltyTransactionRepository.findByAccountIdAndIdempotencyKey(5L, "loyalty-op-1"))
                .thenReturn(Optional.empty());
        when(loyaltyRepository.findById(5L)).thenReturn(Optional.of(account));
        when(transactionTemplate.execute(any())).thenAnswer(invocation ->
                ((TransactionCallback<LoyaltyTransaction>) invocation.getArgument(0)).doInTransaction(null));

        LoyaltyServiceImpl service = new LoyaltyServiceImpl(
                loyaltyRepository,
                loyaltyTransactionRepository,
                transactionTemplate,
                Runnable::run
        );
        service.adjustPoints(5L, 25L, "loyalty-op-1");

        assertThat(account.getBalance()).isEqualTo(125L);
        assertThat(account.getTransactions()).hasSize(1);
        assertThat(account.getTransactions().getFirst().getDelta()).isEqualTo(25L);
        assertThat(account.getTransactions().getFirst().getIdempotencyKey()).isEqualTo("loyalty-op-1");
        verify(loyaltyRepository).save(account);
    }

    @Test
    void adjustPointsRejectsNegativeBalance() {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setId(5L);
        account.setBalance(20L);

        when(loyaltyTransactionRepository.findByAccountIdAndIdempotencyKey(5L, "loyalty-op-2"))
                .thenReturn(Optional.empty());
        when(loyaltyRepository.findById(5L)).thenReturn(Optional.of(account));
        when(transactionTemplate.execute(any())).thenAnswer(invocation ->
                ((TransactionCallback<LoyaltyTransaction>) invocation.getArgument(0)).doInTransaction(null));

        LoyaltyServiceImpl service = new LoyaltyServiceImpl(
                loyaltyRepository,
                loyaltyTransactionRepository,
                transactionTemplate,
                Runnable::run
        );

        assertThatThrownBy(() -> service.adjustPoints(5L, -30L, "loyalty-op-2"))
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(InsufficientPointsException.class)
                .rootCause()
                .isInstanceOf(InsufficientPointsException.class)
                .hasMessage("Insufficient loyalty points");
        verify(loyaltyRepository, never()).save(any());
    }

    @Test
    void adjustPointsIsIdempotentForDuplicateKey() {
        LoyaltyTransaction existing = new LoyaltyTransaction();
        existing.setId(99L);
        existing.setDelta(25L);
        existing.setIdempotencyKey("loyalty-op-3");

        when(loyaltyTransactionRepository.findByAccountIdAndIdempotencyKey(5L, "loyalty-op-3"))
                .thenReturn(Optional.of(existing));

        LoyaltyServiceImpl service = new LoyaltyServiceImpl(
                loyaltyRepository,
                loyaltyTransactionRepository,
                transactionTemplate,
                Runnable::run
        );
        service.adjustPoints(5L, 25L, "loyalty-op-3");

        verify(loyaltyRepository, never()).findById(any());
        verify(loyaltyRepository, never()).save(any());
        verify(transactionTemplate, never()).execute(any());
        verify(loyaltyTransactionRepository).findByAccountIdAndIdempotencyKey(5L, "loyalty-op-3");
    }
}
