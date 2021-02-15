package yaddoong.feemanage.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserFeeStatusDto {
    private String name;
    private int price;
    private int unpaid;

    @QueryProjection
    public UserFeeStatusDto(String name, int price, int unpaid) {
        this.name = name;
        this.price = price;
        this.unpaid = unpaid;
    }
}
