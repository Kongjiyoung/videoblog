package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardJPARepository boardJPARepository;
    private static final String FOLDER = "src/main/resources/static/video/";

    @Transactional
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, BoardRequest.UpdateDTO request) {
        System.out.println("request = " + request);
        Board board = boardJPARepository.findById(id).orElseThrow();
        System.out.println("board = " + board);;

        //비디오 파일 삭제
        Path fileToDelete = Paths.get(FOLDER, board.getVideo());

        try {
            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Please select a file to upload");

        }


        //board 수정
        board.setTitle(request.getTitle());
        board.setVideo(request.getVideo().getOriginalFilename());
        System.out.println("board = " + board);
        if (request.getVideo().isEmpty()) {
            new Exception("Please select a file to upload");
        }

        try {
            // 디렉토리 생성
            Path uploadPath = Paths.get(FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            byte[] bytes = request.getVideo().getBytes();
            Path path = uploadPath.resolve(request.getVideo().getOriginalFilename());
            Files.write(path, bytes);


        } catch (IOException e) {
            throw new IllegalArgumentException("Please select a file to upload");
        }

        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        Board board = boardJPARepository.findById(id).orElseThrow();
        request.setAttribute("board", board);

        return "board/update-form";
    }

    @Transactional
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        Board board = boardJPARepository.findById(id).orElseThrow();


        //비디오 파일 삭제
        Path fileToDelete = Paths.get(FOLDER, board.getVideo());

        try {
            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Please select a file to upload");
        }

        boardJPARepository.deleteById(id);
        return "redirect:/";
    }

    @Transactional
    @PostMapping("/board/upload")
    public String fileUpload(BoardRequest.SavaDTO request) throws Exception {
        System.out.println("request = " + request);
        Board board=boardJPARepository.save(request.toEntity());
        System.out.println("board = " + board);
        if (request.getVideo().isEmpty()) {
            throw new Exception("Please select a file to upload");
        }

        try {
            // 디렉토리 생성
            Path uploadPath = Paths.get(FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            byte[] bytes = request.getVideo().getBytes();
            Path path = uploadPath.resolve(request.getVideo().getOriginalFilename());
            Files.write(path, bytes);


        } catch (IOException e) {
            throw new IllegalArgumentException("Please select a file to upload");
        }

        return "redirect:/";
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        List<Board> boards=boardJPARepository.findAll();
        request.setAttribute("boards", boards);
        return "index";  // Mustache 템플릿 파일 이름 (확장자 제외)

    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        Board board=boardJPARepository.findById(id).orElseThrow();
        request.setAttribute("board", board);
        return "board/detail";
    }
}