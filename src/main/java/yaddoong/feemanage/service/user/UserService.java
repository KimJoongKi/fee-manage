package yaddoong.feemanage.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.domain.fee.SecessionFeeLog;
import yaddoong.feemanage.domain.fee.SecessionFeeLogRepository;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.domain.user.UserRepository;
import yaddoong.feemanage.service.fee.FeeService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PropertySource("classpath:application-param.properties")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FeeLogRepository feeLogRepository;

    private final SecessionFeeLogRepository secessionFeeLogRepository;

    @Value("${fee.price}")
    int feePrice;

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
        List<FeeLog> allByContents = feeLogRepository.findAllByContents(
                findUser.get()
                        .getName());
        List<SecessionFeeLog> list = new ArrayList<>();
        allByContents.stream()
                .forEach(feeLog -> {
                    list.add(SecessionFeeLog.builder()
                            .date(feeLog.getDate())
                            .contents(feeLog.getContents())
                            .afterBalance(feeLog.getAfterBalance())
                            .division(feeLog.getDivision())
                            .memo(feeLog.getMemo())
                            .price(feeLog.getPrice())
                            .build());
                });
        feeLogRepository.deleteFeeLogByContents(findUser.get().getName());
        secessionFeeLogRepository.saveAll(list);

        findUser.get().updateUnpaid(secessionUnpaidCalc(findUser, secessionDate));
        findUser.get().updateSecessionDate(secessionDate);
    }

    @Transactional
    public void rejoin(Long id, String rejoinDate) {
        Optional<User> findUser = userRepository.findById(id);
        List<FeeLog> allByContents = feeLogRepository.findAllByContentsAndDateLessThan(findUser.get().getName(), LocalDate.parse(rejoinDate));
        List<SecessionFeeLog> list = new ArrayList<>();
        allByContents.stream()
                .forEach(feeLog -> {
                    list.add(SecessionFeeLog.builder()
                            .date(feeLog.getDate())
                            .contents(feeLog.getContents())
                            .afterBalance(feeLog.getAfterBalance())
                            .division(feeLog.getDivision())
                            .memo(feeLog.getMemo())
                            .price(feeLog.getPrice())
                            .build());
                });
        feeLogRepository.deleteFeeLogByContentsAndDateLessThan(findUser.get().getName(), LocalDate.parse(rejoinDate));
        secessionFeeLogRepository.saveAll(list);
        findUser.get().updateSecessionDate(null);
        findUser.get().updateJoinDate(rejoinDate);
        findUser.get().updateUnpaid(0);
    }

    // 탈퇴시 미납 금액 계산 로직
    private int secessionUnpaidCalc(Optional<User> findUser, String secessionDateStr) {
        LocalDate secessionLocalDate = LocalDate.parse(secessionDateStr);
        int standardFeePrice = feePriceCalc(findUser.get().getJoinDate().toString(), secessionLocalDate);
        int depositFeePrice = feeLogRepository.findFeePrice(findUser.get().getName());
        return standardFeePrice - depositFeePrice + findUser.get().getUnpaid();
    }

    public int feePriceCalc(String startDateStr, LocalDate endDay) {
        LocalDate startDay = LocalDate.parse(startDateStr);
        int todayMonth = endDay.getMonthValue();
        int startMonth = startDay.getMonthValue()+1;
        int todayDay = endDay.getDayOfMonth();
        int todayYear = endDay.getYear();
        int startYear = startDay.getYear();

        int cnt = (todayDay >= 15 ? 1 : 0)
                + (todayMonth - startMonth)
                + (todayYear - startYear) * 12;

        return cnt * feePrice;
    }
}
