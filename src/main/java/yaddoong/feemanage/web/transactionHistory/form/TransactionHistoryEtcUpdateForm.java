package yaddoong.feemanage.web.transactionHistory.form;

import lombok.Getter;
import lombok.Setter;

/**
 * 기타내역 수정 시 사용하는 객체
 */
@Getter
@Setter
public class TransactionHistoryEtcUpdateForm {
    private Long id;
    private String memo;

}
