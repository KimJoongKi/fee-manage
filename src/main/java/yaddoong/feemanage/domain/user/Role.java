package yaddoong.feemanage.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;

@Getter
@RequiredArgsConstructor
public enum Role {

    OWNER("ROLE_ADMIN", "회장"),
    GENERAL("ROLE_GENERAL", "총무"),
    USER("ROLE_USER", "회원");

    private final String key;
    private final String title;

}
