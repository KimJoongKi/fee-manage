package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FeeCodeRepository extends JpaRepository<FeeCode, Long> {
    Optional<FeeCode> findAllByName(String name);
}
