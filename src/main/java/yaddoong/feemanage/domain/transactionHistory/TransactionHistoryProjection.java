package yaddoong.feemanage.domain.transactionHistory;

/**
 * JPA에서 값을 가져오기 위한 함수를 선언한 class 파일
 */
public interface TransactionHistoryProjection {
    String getName();
    Integer getPrice();
    Integer getUnpaid();
    String getJoinDate();
}
