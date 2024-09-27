package sg.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.funding.AdminSummaryDto;
import sg.backend.service.AdminSummaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminSummaryController {

    private final AdminSummaryService adminSummaryService;

    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryDto> getAdminSummary(){
        AdminSummaryDto summary = adminSummaryService.getAdminSummary();
        return ResponseEntity.ok(summary);
    }


}
