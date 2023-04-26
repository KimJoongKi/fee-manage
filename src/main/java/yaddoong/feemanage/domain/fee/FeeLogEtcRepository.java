package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeLogEtcRepository extends JpaRepository<FeeLogEtc, Long>, FeeLogEtcRepositoryCustom {
}
