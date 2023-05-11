package yaddoong.feemanage.domain.transactionHistory;

import java.util.List;

public interface TransactionHistoryEtcRepositoryCustom {
    List<TransactionHistory> findTransactionHistoryEtc(List<String> contents);
}
