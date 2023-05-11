package yaddoong.feemanage.service.transactionHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yaddoong.feemanage.domain.transactionHistory.*;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.transactionHistory.dto.TransactionHistoryQueryDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 거래내역 서비스 class
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final TransactionHistoryEtcRepository transactionHistoryEtcRepository;

    private final UserService userService;

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

    /**
     * 모든 기타내역을 조회하는 메서드
     * @return
     */
    public List<TransactionHistoryEtc> findTransactionHistoryEtcAll() {
        return transactionHistoryEtcRepository.findAll();
    }

    @Transactional
    public void transactionHistoryEtcAllUpdate() {
        transactionHistoryEtcRepository.deleteAll();
        transactionHistoryEtcsSaveAll();
    }

    /**
     * 회비내역에서 기타내역 추출하여 입력하기
     */
    public void transactionHistoryEtcsSaveAll() {
        List<String> userNames = userService.findUserNames();
        List<TransactionHistory> transactionHistory = findTransactionHistoryEtc(userNames);
        List<TransactionHistoryEtc> transactionHistoryEtc = putTransactionHistoryEtc(transactionHistory);

        transactionHistoryEtcRepository.saveAll(transactionHistoryEtc);
    }

    public List<TransactionHistoryEtc> putTransactionHistoryEtc(List<TransactionHistory> transactionHistory) {
        List<TransactionHistoryEtc> transactionHistoryEtcs =
                new ArrayList<>();
        transactionHistory.forEach(etc ->
                {
                    TransactionHistoryEtc transactionHistoryEtc =
                            TransactionHistoryEtc
                                    .builder()
                                    .date(etc.getDate())
                                    .contents(etc.getContents())
                                    .division(etc.getDivision())
                                    .price(etc.getPrice())
                                    .afterBalance(etc.getAfterBalance())
                                    .memo(etc.getMemo())
                                    .build();
                    transactionHistoryEtcs.add(transactionHistoryEtc);
                }
                );
        return transactionHistoryEtcs;
    }


    /**
     * 기타내역 조회 메서드
     * @param userNames
     * @return
     */
    public List<TransactionHistory> findTransactionHistoryEtc(List<String> userNames) {
        return transactionHistoryEtcRepository.findTransactionHistoryEtc(userNames);
    }

}
