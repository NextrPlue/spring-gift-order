package gift.controller;

import gift.dto.MemberResponse;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${kakao.javascript-id}")
    private String kakaoJavaScriptKey;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoJavaScriptKey", kakaoJavaScriptKey);
        model.addAttribute("redirectUri", redirectUri);
        return "login";
    }

    @GetMapping("/oauth/kakao")
    public String oauthKakao(@RequestParam("code") String code, HttpSession session, Model model) {
        try {
            String token = memberService.kakaoLogin(code);
            session.setAttribute("token", token);
            return "redirect:/wishes/items";
        } catch (Exception e) {
            return "redirect:/members/login?error";
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
            Long memberId = Long.parseLong(claims.getSubject());
            return ResponseEntity.ok("Token is valid for member ID: " + memberId);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Invalid token");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findById(id);
        return ResponseEntity.ok(memberResponse);
    }
}
