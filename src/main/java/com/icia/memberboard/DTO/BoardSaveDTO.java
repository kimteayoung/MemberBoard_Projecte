package com.icia.memberboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardSaveDTO {
    private String boardWriter;
    private String boardPassword;
    private String boardTitle;
    private String boardContents;
    // 이미지 업로드
    // 파일자체
    private MultipartFile boardFile;
    private String boardFileName;
}
