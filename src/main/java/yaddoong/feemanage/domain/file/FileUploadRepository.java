package yaddoong.feemanage.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 파일 업로드 repository 파일
 */
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    
}
