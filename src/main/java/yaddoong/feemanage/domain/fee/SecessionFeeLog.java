package yaddoong.feemanage.domain.fee;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import yaddoong.feemanage.domain.user.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@IdClass(FeeLogId.class)
@Getter
@NoArgsConstructor
@Entity
public class SecessionFeeLog {

    @Id
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
    private LocalDateTime date;
    @Id
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

    @Builder
    public SecessionFeeLog(LocalDateTime date, String contents, String division, int price, int afterBalance, String memo, Member member) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
