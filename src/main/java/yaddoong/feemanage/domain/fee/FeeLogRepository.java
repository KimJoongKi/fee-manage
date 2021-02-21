package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yaddoong.feemanage.web.dto.UserFeeStatusDto;

import java.util.List;

public interface FeeLogRepository extends JpaRepository<FeeLog, Long>, FeeLogRepositoryCustom  {
    @Query(value = "select u.name, COALESCE(sum(f.price),0) AS price, u.unpaid from user as u left outer join fee_log as f on u.name = f.contents " +
            "where u.secession_date is null and (f.memo = '' or f.memo is null) group by u.name order by u.name"
            , nativeQuery = true)
    List<FeeLogProjection> findGroupByName();
}
