package yaddoong.feemanage.domain.transactionHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryEtcRepository extends JpaRepository<TransactionHistoryEtc, Long>, TransactionHistoryEtcRepositoryCustom {
}
