package yaddoong.feemanage.domain.fee;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@IdClass(FeeLogId.class)
@Getter
@NoArgsConstructor
@Entity
public class FeeLog extends BaseTimeEntity {

    @Id
    private Date dealDate;
    @Id
    private String dealContents;
    private String division;
    private int dealPrice;
    private int dealAfterBalance;
    private String memo;

    @Builder
    public FeeLog(Date dealDate, String dealContents, String division, int dealPrice, int dealAfterBalance, String memo) {
        this.dealDate = dealDate;
        this.dealContents = dealContents;
        this.division = division;
        this.dealPrice = dealPrice;
        this.dealAfterBalance = dealAfterBalance;
        this.memo = memo;
    }
}
