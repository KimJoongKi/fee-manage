package yaddoong.feemanage.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.domain.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<String> findUserNames() {
        return userRepository.findUsersNames();
    }


    public List<User> findUserList() {
        return userRepository.findAllBySecessionDateIsNull();
    }

    @Transactional
    public void secessionUpdate(Long id, String secessionDate) {
        Optional<User> findUser = userRepository.findById(id);
        findUser.get().updateSecessionDate(secessionDate);
    }
}
