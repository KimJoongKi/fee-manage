package yaddoong.feemanage.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<String> findUserNames() {
        return userRepository.findUsersNames();
    }

}
