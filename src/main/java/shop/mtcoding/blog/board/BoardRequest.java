package shop.mtcoding.blog.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public class BoardRequest {

    @Data
    public static class SavaDTO{
        private String title;
        private MultipartFile file;


        public Board toEntity(){
            return Board.builder()
                    .title(title)
                    .video(file.getOriginalFilename())
                    .build();
        }
    }
}
