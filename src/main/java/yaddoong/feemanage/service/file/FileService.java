package yaddoong.feemanage.service.file;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.fee.FeeFileLog;
import yaddoong.feemanage.domain.file.FileUpload;
import yaddoong.feemanage.domain.file.FileUploadRepository;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistory;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistoryRepository;
import yaddoong.feemanage.web.file.dto.FileDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FileService {

    // 파일 업로드 repository
    private final FileUploadRepository fileUploadRepository;

    // 거래내역 repository
    private final TransactionHistoryRepository transactionHistoryRepository;

    // 날짜 패턴
    private static final Pattern datePattern = Pattern.compile("\\d{4}.\\d{2}.\\d{2} \\d{2}:\\d{2}:\\d{2}");
    // 숫자 패턴
    private static final Pattern numberPattern = Pattern.compile("[0-9]+");

    /**
     * 파일의 엑셀정보를 입력하는 메소드
     * @param fileDto
     */
    public void saveFileInfo(FileDto fileDto) {
        for (MultipartFile multipartFile : fileDto.getFileList()) {
            insertExcelData(multipartFile, insertExcelUploadFileData(multipartFile));
        }
    }

    /**
     * 엑셀 파일에 입력된 내용을 데이터베이스에 저장하는 메소드
     * @param file
     * @param fileUploadId
     */
    private void insertExcelData(MultipartFile file, Long fileUploadId) {
        XSSFWorkbook excel = null;
        try {
            excel = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        transactionHistoryRepository.saveAll(insertSheetData(fileUploadId, excel));
    }

    /**
     * sheet에 있는 데이터를 입력하는 메소드
     * @param fileUploadId
     * @param excel
     * @return
     */
    private List<TransactionHistory> insertSheetData(Long fileUploadId, XSSFWorkbook excel) {
        XSSFSheet sheet = excel.getSheetAt(0);
        XSSFRow row = sheet.getRow(10);
        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = (int) row.getLastCellNum();

        Matcher matcher = null;
        List<TransactionHistory> list = new ArrayList<>();
        for (int i = 11; i <= lastRowNum; i++) {
            XSSFRow selectRow = sheet.getRow(i);
            Map<String, Object> map = new HashMap<>();
            map.put("normal", true);
            map.put("fileUploadId", fileUploadId);
            for (int j = 1; j <= lastCellNum; j++) {
                XSSFCell cell = selectRow.getCell(j);
                switch (j) {
                    case 1:
                        matcher = datePattern.matcher(cell.getStringCellValue());
                        if (!matcher.find()) {
                            map.put("normal", false);
                        }
                        map.put("date", matcher.group());
                        break;
                    case 2:
                        map.put("division", cell.getStringCellValue());
                        break;
                    case 3:
                        matcher = numberPattern.matcher(cell.getStringCellValue().replaceAll(",",""));
                        if (!matcher.find()) {
                            map.put("normal", false);
                        }
                        map.put("price", matcher.group().replaceAll("[^0-9]", ""));
                        break;
                    case 4:
                        matcher = numberPattern.matcher(cell.getStringCellValue().replaceAll(",",""));
                        if (!matcher.find()) {
                            map.put("normal", false);
                        }
                        map.put("afterBalance", matcher.group().replaceAll("[^0-9]", ""));
                        break;
                    case 6:
                        map.put("contents", cell.getStringCellValue());
                        break;
                    case 7:
                        map.put("memo", cell.getStringCellValue());
                        break;
                }
            }
            list.add(TransactionHistory.builder().map(map).build());
        }
        return list;
    }

    /**
     * 업로드 파일 정보를 입력하는 메소드
     * @param file
     * @return
     */
    private Long insertExcelUploadFileData(MultipartFile file) {

        Map<String, Object> uploadFileMap = new HashMap<>();
        uploadFileMap.put("filename", file.getOriginalFilename()); // 파일명
        uploadFileMap.put("uploadDateTime", LocalDateTime.now()); // 업로드 시간
        return fileUploadRepository.save(
                        FileUpload.builder()
                                .map(uploadFileMap)
                                .build())
                .getId();
    }

    /**
     * 파일 업로드 로그 가져오기
     * @param pageable
     * @return
     */
    public Page<FileUpload> selectFileUploadLog(Pageable pageable) {
        return fileUploadRepository.findAll(pageable);
    }

}
