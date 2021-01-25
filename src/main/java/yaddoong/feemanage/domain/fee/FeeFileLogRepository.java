package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FeeFileLogRepository extends PagingAndSortingRepository<FeeFileLog, Long> {
}
