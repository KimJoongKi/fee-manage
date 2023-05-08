package yaddoong.feemanage.domain.transactionHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.transactionHistory.QTransactionHistory.transactionHistory;

public class TransactiobnHistoryRepositoryImpl implements TransactiobnHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TransactiobnHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 1. 카카오 거래내역 내용란에 회원의 이름이 있으면 회비로 간주합니다.
     * 2. 총무가 회원이 본인의 이름이 아닌 이름으로 입금을 하거나 다른 용도로 회원에게 돈을
     * 보내거나 받은 경우 메모란에 입력을 하기 때문에 내용이 회원이름이 아니거나 메모가 비어 있는
     * 것들을 회비가 아닌 것으로 가정합니다.
     * @param contents
     * @return
     */
    @Override
    public List<TransactionHistory> findTransactionHistory(List<String> contents) {
        List<TransactionHistory> transactionHistories = queryFactory
                .select(transactionHistory)
                .from(transactionHistory)
                .where(
                        transactionHistory
                                .contents
                                .notIn(contents) // 모임 회원 이름 목록을 조건에 넣습니다.
                                .and(transactionHistory.memo.eq("")) // 메모에 아무것도 입력되지 않은 내용은 구분이 필요하기에 조회합니다.
                )
                .fetch();
        return transactionHistories;
    }
}
