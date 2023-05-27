package yaddoong.feemanage.domain.transactionHistory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 엑셀에 저장되어 있는 정보 class 파일
 */
@IdClass(TransactionHistoryId.class)
@Getter
@NoArgsConstructor
@Entity
@ToString
public class TransactionHistory {
    @Id @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
    private LocalDateTime date; // 거래일시
    @Id
    private String contents; // 내용
    private String division; // 입출금 구분
    private int price; // 금액
    private int afterBalance; // 잔액
    private String memo; // 메모
    private boolean normal; // 내용의 숫자 여부
    private Long fileUploadId; // 업로드 파일 ID

    @Builder
    public TransactionHistory(Map<String, Object> map) {
        this.date = LocalDateTime.parse(map.get("date").toString(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.contents = map.get("contents").toString();
        this.division = map.get("division").toString();
        this.price = Integer.parseInt(map.get("price").toString());
        this.afterBalance = Integer.parseInt(map.get("afterBalance").toString());
        this.memo = map.get("memo").toString();
        this.normal = (boolean) map.get("normal");
        this.fileUploadId = Long.parseLong(map.get("fileUploadId").toString());
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
