package yaddoong.feemanage.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.user.Role;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FeeLogDto {
    private Date date;
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

    @Builder
    public FeeLogDto(Date date, String contents, String division, int price, int afterBalance, String memo) {
        this.date = date;
        this.contents = contents;
        this.division = division;
        this.price = price;
        this.afterBalance = afterBalance;
        this.memo = memo;
    }

    public FeeLog toEntity() {
        return FeeLog.builder()
                .date(date)
                .contents(contents)
                .division(division)
                .price(price)
                .afterBalance(afterBalance)
                .memo(memo)
                .build();
    }

}
