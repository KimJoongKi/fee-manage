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

        // TODO: 2021/05/17 금액을 가져오는 것까지는 했는데 이걸 객체에 담아서 화면에 뿌려줘야함 
        List<FeeCodeStatisticsDto> list = queryFactory
                .select(new QFeeCodeStatisticsDto(feeCode.id, feeCode.gubun, feeCode.name, feeDetailGubun.price.sum()))
                .from(feeDetailGubun)
                .innerJoin(feeDetailGubun.code, feeCode)
                .groupBy(feeCode.id, feeCode.name)
                .orderBy(feeCode.gubun.asc(),feeDetailGubun.price.sum().desc())
                .fetch();

        return list;
    }
}
