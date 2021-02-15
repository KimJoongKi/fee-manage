package yaddoong.feemanage.domain.fee;

import java.util.List;

public interface FeeLogRepositoryCustom {

    List<UserFeeStatusInterface> findGroupByName();

}
