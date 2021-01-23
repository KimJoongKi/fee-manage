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
public class FeeLog {

    @Id
    private Date date;
    @Id
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

    @Builder
    public FeeLog(Date date, String contents, String division, int price, int afterBalance, String memo) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
    }
}
