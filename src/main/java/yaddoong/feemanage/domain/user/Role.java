package yaddoong.feemanage.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN", "회장"),
    ROLE_GENERAL("ROLE_GENERAL", "총무"),
    ROLE_USER("ROLE_USER", "회원");

    private final String key;
    private final String title;

}
