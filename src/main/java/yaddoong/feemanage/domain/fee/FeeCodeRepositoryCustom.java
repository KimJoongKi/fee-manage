package yaddoong.feemanage.domain.fee;

import java.util.Optional;

public interface FeeCodeRepositoryCustom {
    Optional<FeeCode> findFeeCodeMemoAndDivision(String memo, String division);


}
