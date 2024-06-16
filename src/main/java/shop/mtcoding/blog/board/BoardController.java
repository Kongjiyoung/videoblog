package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, String title, String content) {
        Board board = boardJPARepository.findById(id).orElseThrow();
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        Board board = boardJPARepository.findById(id).orElseThrow();
        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        boardJPARepository.deleteById(id);
        return "redirect:/";
    }


    @PostMapping("/board/upload")
    public String fileUpload(BoardRequest.SavaDTO request) {
        boardJPARepository.save(request.toEntity());
        if (request.getFile().isEmpty()) {
            new Exception("Please select a file to upload");
        }

        try {
            // 디렉토리 생성
            Path uploadPath = Paths.get(FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            byte[] bytes = request.getFile().getBytes();
            Path path = uploadPath.resolve(request.getFile().getOriginalFilename());
            Files.write(path, bytes);


        } catch (IOException e) {
            e.printStackTrace();
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
    public String detail(@PathVariable String videoName, HttpServletRequest request) {
        request.setAttribute("videoName", videoName);
        return "board/detail";
    }

    @GetMapping("/board/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}
