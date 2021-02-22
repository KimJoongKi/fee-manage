package yaddoong.feemanage.domain.fee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class FeeLogEtc {

    @Id @GeneratedValue
    private Long id;
    private Date date;
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

    @Builder
    public FeeLogEtc(Date date, String contents, String division, int price, int afterBalance, String memo) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
    }
}
