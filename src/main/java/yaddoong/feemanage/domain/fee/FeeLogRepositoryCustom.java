package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.Query;
import yaddoong.feemanage.web.dto.UserFeeStatusDto;

import java.util.List;

public interface FeeLogRepositoryCustom {
    List<FeeLog> findFeeLogEtc(List<String> contents);
}
