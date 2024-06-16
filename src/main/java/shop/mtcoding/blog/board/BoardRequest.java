package shop.mtcoding.blog.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public class BoardRequest {

    @Data
    public static class SavaDTO{
        private String title;
        private MultipartFile video;


        public Board toEntity(){
            return Board.builder()
                    .title(title)
                    .video(video.getOriginalFilename())
                    .build();
        }
    }

    @Data
    public static class UpdateDTO{
        private String title;
        private MultipartFile video;
    }
}
