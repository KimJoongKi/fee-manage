package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.domain.user.UserRepositoryCustom;

public interface FeeLogEtcRepository extends JpaRepository<FeeLogEtc, Long>, FeeLogEtcRepositoryCustom {
}
