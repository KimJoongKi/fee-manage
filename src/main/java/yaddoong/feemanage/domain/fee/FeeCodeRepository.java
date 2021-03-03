package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FeeCodeRepository extends JpaRepository<FeeCode, Long> {
}
