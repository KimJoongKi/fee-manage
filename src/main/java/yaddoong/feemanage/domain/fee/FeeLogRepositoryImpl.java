package yaddoong.feemanage.domain.fee;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.fee.QFeeLog.feeLog;

public class FeeLogRepositoryImpl implements FeeLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FeeLogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FeeLog> findFeeLogEtc(List<String> usernames) {
        List<FeeLog> feeLogList = queryFactory
                .select(feeLog)
                .from(feeLog)
                .where(
                        feeLog
                                .contents
                                .notIn(usernames).and(feeLog.memo.eq(""))
                )
                .fetch();
        return feeLogList;
    }
}
