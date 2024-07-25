package gift.service;

import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, KakaoService kakaoService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.kakaoService = kakaoService;
    }

    public String register(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(memberRequest.password());
        Member member = new Member(memberRequest.email(), encodedPassword);
        memberRepository.save(member);
        return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
    }

    public String authenticate(MemberRequest memberRequest) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberRequest.email());
        if (optionalMember.isPresent() && passwordEncoder.matches(memberRequest.password(), optionalMember.get().getPassword())) {
            Member member = optionalMember.get();
            return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<WishResponse> wishResponses = member.getWishes().stream()
                .map(wish -> new WishResponse(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(), wish.getProductNumber()))
                .collect(Collectors.toList());
        return new MemberResponse(member.getId(), member.getEmail(), wishResponses);
    }

    public String kakaoLogin(String code) {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        Long kakaoId = (Long) userInfo.get("id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = null;
        if (profile != null) {
            nickname = (String) profile.get("nickname");
        }

        if (nickname == null) {
            throw new RuntimeException("Failed to retrieve user nickname");
        }

        String email = "kakao_" + kakaoId + "_" + nickname.replaceAll("[^a-zA-Z0-9]", "_") + "@kakao.com";

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
        } else {
            member = new Member(email, passwordEncoder.encode(UUID.randomUUID().toString()));
            memberRepository.save(member);
        }

        return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
    }
}
