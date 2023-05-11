package yaddoong.feemanage.domain.transactionHistory;

import java.util.List;

public interface TransactionHistoryRepositoryCustom {
    List<TransactionHistory> findTransactionHistory(List<String> contents);

}
