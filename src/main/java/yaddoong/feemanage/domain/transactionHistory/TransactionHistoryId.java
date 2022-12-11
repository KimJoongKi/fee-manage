package yaddoong.feemanage.domain.transactionHistory;

import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 거래내역 ID class 파일
 * 중복된 거래내역을 제거하기 위하여 일시와 내용을 합쳐서 ID 값으로 지정
 */
@NoArgsConstructor
public class TransactionHistoryId implements Serializable {
    @Id
    private LocalDateTime date; // 일시
    @Id
    private String contents; // 내용
}
