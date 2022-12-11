package yaddoong.feemanage.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 파일 업로드 정보 class 파일
 */
@Getter
@NoArgsConstructor
@Entity
public class FileUpload {
    @Id
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
    @GeneratedValue
    private Long id; // 파일 업로드 id

    private String filename; // 파일 명

    private LocalDateTime uploadDateTime; // 파일 업로드 시간

    @Builder
    public FileUpload(Map<String, Object> map) {
        this.filename = map.get("filename").toString();
        this.uploadDateTime = (LocalDateTime) map.get("uploadDateTime");
    }
}
