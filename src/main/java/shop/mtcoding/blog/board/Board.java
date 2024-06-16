package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String video;
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, String title, String video, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.createdAt = createdAt;
    }

}
