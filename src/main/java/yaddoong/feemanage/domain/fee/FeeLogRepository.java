package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeLogRepository extends JpaRepository<FeeLog, Long> , FeeLogRepositoryCustom {

    List<UserFeeStatusInterface> findGroupByName();

}
