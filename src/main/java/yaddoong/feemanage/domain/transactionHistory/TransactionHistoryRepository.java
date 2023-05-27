package yaddoong.feemanage.domain.transactionHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 거래내역 repository class 파일
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findTransactionHistoriesByDateBetweenAndContentsLikeOrderByDateDesc(LocalDateTime startLocalDate, LocalDateTime endLocalDate, String contents);

    /**
     * 같은 시간에 같은 내용 내역 조회
     * @param contents
     * @param date
     * @return
     */
    Optional<TransactionHistory> findTransactionHistoryByContentsAndDate(String contents, LocalDateTime date);

    @Query(value = "select u.name, COALESCE(sum(f.price),0) AS price, u.unpaid, join_date AS joinDate from member as u left outer join transaction_history as f on u.name = f.contents " +
            "where u.secession_date is null and (f.memo = '' or f.memo is null) group by u.name order by u.name"
            , nativeQuery = true)
    List<TransactionHistoryProjection> findGroupByName(); // 현재 가입 중인 회원의 회비 납부 내역

}
