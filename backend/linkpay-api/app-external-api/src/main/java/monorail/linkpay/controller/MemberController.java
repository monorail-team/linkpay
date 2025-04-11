package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.member.dto.MemberResponse;
import monorail.linkpay.member.dto.EmailsResponse;
import monorail.linkpay.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> getMember(@RequestParam final String email) {
        return ResponseEntity.ok(memberService.getMember(email));
    }

    @GetMapping("search-email")
    public ResponseEntity<EmailsResponse> searchEmail(@RequestParam final String keyword,
                                                      @RequestParam final int size
                                                               ) {
        var response = memberService.searchEmail(keyword, size);
        return ResponseEntity.ok(response);
    }
}
