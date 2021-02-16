package yaddoong.feemanage.domain.fee;

import yaddoong.feemanage.web.dto.UserFeeStatusDto;

import java.util.List;

public interface FeeLogRepositoryCustom {

    List<UserFeeStatusDto> findGroupByName();

}
