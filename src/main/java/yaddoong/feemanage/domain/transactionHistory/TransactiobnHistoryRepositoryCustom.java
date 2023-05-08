package yaddoong.feemanage.domain.transactionHistory;

import java.util.List;

public interface TransactiobnHistoryRepositoryCustom {
    List<TransactionHistory> findTransactionHistory(List<String> contents);

}
