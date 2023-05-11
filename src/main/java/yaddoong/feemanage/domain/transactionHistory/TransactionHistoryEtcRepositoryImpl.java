package yaddoong.feemanage.domain.transactionHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.transactionHistory.QTransactionHistory.transactionHistory;

public class TransactionHistoryEtcRepositoryImpl implements TransactionHistoryEtcRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TransactionHistoryEtcRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TransactionHistory> findTransactionHistoryEtc(List<String> contents) {
        return queryFactory
                .select(transactionHistory)
                .from(transactionHistory)
                .where(
                        transactionHistory
                                .contents
                                .notIn(contents)
                                .and(transactionHistory.memo.eq(""))
                )
                .fetch();
    }
}
