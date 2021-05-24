package yaddoong.feemanage.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class FeeCodeStatisticsDto {
    private Long id;
    private String gubun;
    private String name;
    private int priceSum;

    @QueryProjection
    public FeeCodeStatisticsDto(Long id, String gubun, String name, int priceSum) {
        this.id = id;
        this.gubun = gubun;
        this.name = name;
        this.priceSum = priceSum;
    }
}
