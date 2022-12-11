package yaddoong.feemanage.service.transactionhistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistory;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistoryProjection;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistoryRepository;
import yaddoong.feemanage.web.transactionhistory.dto.TransactionHistoryQueryDto;

import java.util.List;

/**
 * 거래내역 서비스 class
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    /**
     * 거래내역 조회 메소드
     * @param dto
     * @return
     */
    public List<TransactionHistory> getTransactionHistoryList(TransactionHistoryQueryDto dto) {
        return transactionHistoryRepository.findTransactionHistoriesByDateBetweenAndContentsLikeOrderByDateDesc(dto.getStartLocalDate(), dto.getEndLocalDate(), dto.getLikeContents());
    }

    /**
     * 사용자별 거래내역 조회 메소드
     * @return
     */
    public List<TransactionHistoryProjection> findGroupByName() {
        return transactionHistoryRepository.findGroupByName();
    }
}
