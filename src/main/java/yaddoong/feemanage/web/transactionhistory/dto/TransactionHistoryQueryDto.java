package yaddoong.feemanage.web.transactionhistory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.web.transactionhistory.form.TransactionHistorySearchForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class TransactionHistoryQueryDto {

    private String contents;
    private LocalDateTime startLocalDate;
    private LocalDateTime endLocalDate;

    @Builder
    public TransactionHistoryQueryDto(TransactionHistorySearchForm form) {
        this.contents = form.getContents();
        this.startLocalDate = LocalDate.parse(form.getStartDateTime()).atTime(LocalTime.MIN);
        this.endLocalDate = LocalDate.parse(form.getEndDateTime()).atTime(LocalTime.MAX);
    }

    /**
     * 쿼리의 like 조건문에 들어갈 문자열을 만드는 메소드
     * @return
     */
    public String getLikeContents() {
        return "%" + this.contents + "%";
    }
}
