package yaddoong.feemanage.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class FeeCodeStatisticsDto {
    private Long id;
    private String insertFlag;
    private String name;
    private String graphColumn;
    private int priceSum;
    private int graphPriceSum;

    @QueryProjection
    public FeeCodeStatisticsDto(Long id, String insertFlag, String name, int priceSum) {
        this.id = id;
        this.insertFlag = insertFlag;
        this.name = name;
        this.graphColumn = name + "(" + insertFlag + ")";
        this.graphPriceSum = Math.abs(priceSum);
        this.priceSum = priceSum;
    }
}
