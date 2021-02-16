package yaddoong.feemanage.domain.fee;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import yaddoong.feemanage.domain.base.BaseTimeEntity;
import yaddoong.feemanage.domain.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    public FeeLog(Date date, String contents, String division, int price, int afterBalance, String memo, User user) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
    }
}
