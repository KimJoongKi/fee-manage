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
import yaddoong.feemanage.web.dto.FeeLogDto;
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

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@Controller
public class FeeController {

    private final FeeService feeService;
    private final FeeCodeRepository feeCodeRepository;
    private final FeeDetailGubunRepository feeDetailGubunRepository;

    /**
     * 파일업로드 화면
     * @return
     */
    @GetMapping(value = "/new")
    public String saveForm() {
        return "fee/saveForm";
    }

    /**
     * 입출금내역 저장
     * @param file
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @PostMapping("/save")
    public String insertFeeLog(@RequestParam("file") MultipartFile[] file) throws Exception {
        feeService.saveAll(file);
        return "redirect:/";
    }

    /**
     * 통장 입출금내역 조회
     * @param model
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/list")
    public String listView(Model model) {

        LocalDateTime startDate = LocalDate.now().minusMonths(6).atTime(LocalTime.MIN);
        LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);
        String contents = "%%";

        FeeLogForm form = new FeeLogForm(startDate, endDate);
        List<FeeLog> feeLogs = feeService.findAll(startDate, endDate, contents);

        model.addAttribute("form", form);
        model.addAttribute("feeLogs", feeLogs);

        return "fee/list";

    }

    /**
     * 통장 입출금내역 조회
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/list")
    public String searchList(FeeLogForm form, Model model) {

        String formStartDate = form.getStartDate();
        String formEndDate = form.getEndDate();
        String formContents = "%" + form.getContents() + "%";

        LocalDateTime startDate = LocalDate.parse(formStartDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atTime(
                        LocalTime.MIN);
        LocalDateTime endDate = LocalDate.parse(formEndDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atTime(
                        LocalTime.MAX);

        List<FeeLog> feeLogs = feeService.findAll(startDate, endDate, formContents);

        model.addAttribute("form", form);
        model.addAttribute("feeLogs", feeLogs);

        return "fee/list";
    }


    /**
     * 회비 미납내역 조회
     * @param model
     * @return
     */
    @GetMapping(value = "/userFeeList")
    public String userFeeList(Model model) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));

        Page<FeeFileLog> all = feeService.findFeeFileLogAll(pageable);
        if (all.getContent().size() == 0) {
            model.addAttribute("lastUpdateData", "업데이트 기록 없음");
            return "fee/userFeeList";
        }
        model.addAttribute
                ("lastUpdateData","(21년 1월 23일부터) " + all.getContent()
                        .get(0)
                        .getName()
                        .replaceAll(".xlsx","") + " 까지 기준");
        List<FeeLogProjection> list = feeService.findGroupByName();
        List<UserFeeForm> feeFormList = new ArrayList<>();
        for (FeeLogProjection feeLogProjection : list) {
            UserFeeForm form = new UserFeeForm();
            form.setName(feeLogProjection.getName());
            form.setPrice(feeLogProjection.getPrice());
            form.setUnpaid(feeService.feePriceCalc(feeLogProjection.getJoinDate())
                    - feeLogProjection.getPrice()
                    + feeLogProjection.getUnpaid());
            feeFormList.add(form);
        }
        model.addAttribute("fees", feeFormList);

        return "fee/userFeeList";
    }

    /**
     * 기타내역 조회
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
     * @return
     */
    @GetMapping("/feeLogEtcAllUpdate")
    public String feeLogEtcAllUpdate() {
        feeService.feeLogEtcAllUpdate();
        return "redirect:/fee/etcList";
    }

    /**
     * 입출금 통계 조회 화면
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

