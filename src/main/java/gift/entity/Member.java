// Member
package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    private String email;
    private String password;
    private String kakaoId;
    private String kakaoNickname;
    private String kakaoToken;

    @OneToMany(mappedBy = "member")
    private List<Wish> wishes = new ArrayList<>();

    public Member() {}

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, String kakaoId, String kakaoNickname, String kakaoToken) {
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
        this.kakaoNickname = kakaoNickname;
        this.kakaoToken = kakaoToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public String getKakaoNickname() {
        return kakaoNickname;
    }

    public void setKakaoNickname(String kakaoNickname) {
        this.kakaoNickname = kakaoNickname;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

    public void setKakaoToken(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }
}
