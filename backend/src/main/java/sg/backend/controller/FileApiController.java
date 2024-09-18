package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.service.FileService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileApiController {

    private final FileService fileService;

    @Operation(
            summary = "파일 가져오기"
    )
    @GetMapping(value = "/{fileName}")
    public Resource getImage(
            @PathVariable("fileName") String fileName
    ) {
        Resource resource = fileService.getImage(fileName);
        return resource;
    }
}
