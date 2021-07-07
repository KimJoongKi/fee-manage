package yaddoong.feemanage.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.domain.user.UserRepository;
import yaddoong.feemanage.service.fee.FeeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FeeLogRepository feeLogRepository;

    public List<String> findUserNames() {
        return userRepository.findUsersNames();
    }

    public List<User> findUserList() {
        return userRepository.findAllBySecessionDateIsNull();
    }

    public List<User> findSecessionUserList() {
        return userRepository.findAllBySecessionDateIsNotNull();
    }

    @Transactional
    public void secession(Long id, String secessionDate) {
        Optional<User> findUser = userRepository.findById(id);
        findUser.get().updateSecessionDate(secessionDate);
    }

    @Transactional
    public void rejoin(Long id, String rejoinDate) {
        Optional<User> findUser = userRepository.findById(id);
        findUser.get().updateSecessionDate(null);
        findUser.get().updateJoinDate(rejoinDate);
    }
}
