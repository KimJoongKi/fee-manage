package yaddoong.feemanage.domain.fee;

import yaddoong.feemanage.web.dto.FeeCodeStatisticsDto;

import java.util.List;

public interface FeeDetailGubunRepositoryCustom {

    List<FeeCodeStatisticsDto> feeDetailGubunGroupByCode();
}
