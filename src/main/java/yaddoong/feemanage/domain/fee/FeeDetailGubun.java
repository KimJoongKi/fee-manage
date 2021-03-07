package yaddoong.feemanage.domain.fee;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FeeDetailGubun {

    @Id @GeneratedValue
    @Column(name = "fee_id")
    private Long id;

    private LocalDateTime date;
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    private FeeCode code;

    public FeeDetailGubun(LocalDateTime date, String contents, String division, int price, int afterBalance, String memo) {
        this(date, contents, division, price, afterBalance, memo, null);
    }

    @Builder
    public FeeDetailGubun(LocalDateTime date, String contents, String division, int price, int afterBalance, String memo, FeeCode code) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
        this.code = code;
    }


}

