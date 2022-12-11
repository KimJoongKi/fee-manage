package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yaddoong.feemanage.web.dto.UserFeeStatusDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FeeLogRepository extends JpaRepository<FeeLog, Long>, FeeLogRepositoryCustom {

    List<FeeLog> findFeeLogsByDateBetweenAndContentsLikeOrderByDateDesc(LocalDateTime sDate, LocalDateTime eDate, String contents);

    Optional<FeeLog> findFeeLogByContentsAndDate(String contents, LocalDateTime date);

    List<FeeLog> findAllByMemoNot(String memo);

    List<FeeLog> findAllByContents(String contents);

    List<FeeLog> findAllByContentsAndDateLessThan(String contents, LocalDateTime date);

    void deleteFeeLogByContents(String contents);

    void deleteFeeLogByContentsAndDateLessThan(String contents, LocalDateTime date);

    @Query(value = "select u.name, COALESCE(sum(f.price),0) AS price, u.unpaid, join_date AS joinDate from user as u left outer join transaction_history as f on u.name = f.contents " +
            "where u.secession_date is null and (f.memo = '' or f.memo is null) group by u.name order by u.name"
            , nativeQuery = true)
    List<FeeLogProjection> findGroupByName();
}
