package yaddoong.feemanage.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.fee.FeeLog;

import javax.persistence.Id;
import java.util.Date;

@Getter
@NoArgsConstructor
public class FeeLogSaveDto {
    private Date dealDate;
    private String dealContents;
    private String division;
    private int dealPrice;
    private int dealAfterBalance;
    private String memo;

    @Builder
    public FeeLogSaveDto(Date dealDate, String dealContents, String division, int dealPrice, int dealAfterBalance, String memo) {
        this.dealDate = dealDate;
        this.dealContents = dealContents;
        this.division = division;
        this.dealPrice = dealPrice;
        this.dealAfterBalance = dealAfterBalance;
        this.memo = memo;
    }

    public FeeLog toEntity() {
        return FeeLog.builder()
                .dealDate(dealDate)
                .dealContents(dealContents)
                .division(division)
                .dealPrice(dealPrice)
                .dealAfterBalance(dealAfterBalance)
                .memo(memo)
                .build();
    }

}
