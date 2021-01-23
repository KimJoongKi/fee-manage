package yaddoong.feemanage.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGrade {

    OWNER("ROLE_ADMIN", "회장"),
    GENERAL("ROLE_GENERAL", "총무"),
    USER("ROLE_USER", "회원");

    private final String key;
    private final String title;

}
