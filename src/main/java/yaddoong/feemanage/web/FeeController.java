package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.fee.*;
import yaddoong.feemanage.service.fee.FeeService;
import yaddoong.feemanage.web.dto.FeeCodeStatisticsDto;
import yaddoong.feemanage.web.form.FeeCodeUpdateForm;
import yaddoong.feemanage.web.form.FeeLogEtcUpdateForm;
import yaddoong.feemanage.web.form.FeeLogForm;
import yaddoong.feemanage.web.form.UserFeeForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @RequiredArgsConstructor
 * final필드나 @NonNull이 붙은 필드에 대해 생성자를 생성해줍니다.
 */
@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@Controller
public class FeeController {

    private final FeeService feeService;
    private final FeeCodeRepository feeCodeRepository;
    private final FeeDetailGubunRepository feeDetailGubunRepository;

    /**
     * 기타내역 조회
     *
     * @param model
     * @return
     */
    @GetMapping("/etcList")
    public String etcList(Model model) {

        List<FeeLogEtc> feeLogEtcs = feeService.findFeeLogEtcAll();

        model.addAttribute("feeLogEtcs", feeLogEtcs);
        model.addAttribute("form", new FeeLogEtcUpdateForm());

        return "fee/etcList";

    }

    /**
     * 기타내역 개별 변경
     *
     * @param form
     * @return
     */
    @PostMapping("/feeLogEtcUpdate")
    public String feeLogEtcUpdate(FeeLogEtcUpdateForm form) {

        Optional<FeeLogEtc> feeLogEtc = feeService.findFeeLogEtcById(form.getId());
        feeLogEtc.get().updateMemo(form.getMemo());
        feeService.feeLogAndEtcSave(feeLogEtc.get());

        return "redirect:/fee/etcList";
    }

    /**
     * 기타내역 변경내용 적용
     *
     * @return
     */
    @GetMapping("/feeLogEtcAllUpdate")
    public String feeLogEtcAllUpdate() {
        feeService.feeLogEtcAllUpdate();
        return "redirect:/fee/etcList";
    }

    /**
     * 입출금 통계 조회 화면
     *
     * @return
     */
    @GetMapping("/earningsAndExpenditure")
    public String earningsAndExpenditure() {
        return "fee/earningsAndExpenditureView";
    }

    /**
     * 입출금 내역 새로고침
     */
    @GetMapping("/feeLogRefresh")
    public String logRefresh() {
        feeService.feeLogRefresh();
        return "redirect:/fee/earningsAndExpenditure";
    }

    /**
     * 코드 관리 화면 조회
     *
     * @param model
     * @return
     */
    @GetMapping("/feeCode")
    public String feeCodeView(Model model) {
        // 분류가 필요한 회비 내역 조회
        List<FeeDetailGubun> feeDetailGubuns = feeService.feeCodeView();
        // 입,출금 코드 목록 조회
        List<FeeCode> codeList = feeCodeRepository.findAll();
        List<FeeDetailGubun> detailList = feeDetailGubunRepository.findAll();
        List<FeeCodeStatisticsDto> statList = feeDetailGubunRepository.feeDetailGubunGroupByCode();
        model.addAttribute("detailList", feeDetailGubuns);
        model.addAttribute("statList", statList);
        model.addAttribute("codeList", codeList);
        return "fee/feeCodeList";
    }

    @PostMapping("/feeCodeInsert")
    public String feeCodeInsert(FeeCodeUpdateForm form) {

        FeeCode feeCode = null;

        // 코드 아이디가 있으면, 코드 정보를 가져와서 detail에 추가 후 입력한다.
        if (form.getCodeId() != null) {

            feeService.selectCodeSaveDetail(form.getCodeId(), form.getId());

            return "redirect:/fee/feeCode";
        }

        // 코드 아이디가 없으면, 코드 정보를 입력 후 detail에 입력한다.
        feeService.saveCodeSaveDetail(form.getName(), form.getInsertFlag(), form.getId());

        return "redirect:/fee/feeCode";
    }



}

