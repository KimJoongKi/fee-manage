package yaddoong.feemanage.domain.fee;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import yaddoong.feemanage.web.dto.FeeCodeStatisticsDto;
import yaddoong.feemanage.web.dto.QFeeCodeStatisticsDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static yaddoong.feemanage.domain.fee.QFeeDetailGubun.feeDetailGubun;
import static yaddoong.feemanage.domain.fee.QFeeCode.feeCode;

public class FeeDetailGubunRepositoryImpl implements FeeDetailGubunRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FeeDetailGubunRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FeeCodeStatisticsDto> feeDetailGubunGroupByCode() {

        List<FeeCodeStatisticsDto> list = queryFactory
                .select(new QFeeCodeStatisticsDto(feeCode.id, feeCode.gubun, feeCode.name, feeDetailGubun.price.sum()))
                .from(feeDetailGubun)
                .innerJoin(feeDetailGubun.code, feeCode)
                .groupBy(feeCode.id, feeCode.name)
                .orderBy(feeCode.gubun.desc(),feeDetailGubun.price.sum().asc())
                .fetch();

        return list;
    }
}
