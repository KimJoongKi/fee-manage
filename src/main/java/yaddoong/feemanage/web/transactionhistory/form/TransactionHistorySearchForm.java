package yaddoong.feemanage.web.transactionhistory.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 거래내역 검색 form class 파일
 */
@Getter
@Setter
@NoArgsConstructor
public class TransactionHistorySearchForm {

    private String contents; // 내용
    private String startDateTime; // 검색 시작일시
    private String endDateTime; // 검색 종료일시

    @Builder
    public TransactionHistorySearchForm(Map<String, Object> map) {
        this.contents = map.get("contents").toString();
        this.startDateTime = ((LocalDateTime) map.get("startDateTime")).toLocalDate().format(DateTimeFormatter.ISO_DATE);
        this.endDateTime = ((LocalDateTime) map.get("endDateTime")).toLocalDate().format(DateTimeFormatter.ISO_DATE);
    }

}
