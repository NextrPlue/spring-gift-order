package gift.study;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RestTemplateTest {
    private final RestTemplate client1 = new RestTemplateBuilder().build();
    private final RestClient client2 = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "19376eed1b3349344fe573759afca0a4");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "TYYOcQfRwyVfFjuzOtd6tQ8T8Vy-9BUnh_4ZmF2_ZpcobVzyJqPtuwAAAAQKKcleAAABkN5hhLRV7imzm104lw");
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client1.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    @Test
    void test2() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "19376eed1b3349344fe573759afca0a4");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "TYYOcQfRwyVfFjuzOtd6tQ8T8Vy-9BUnh_4ZmF2_ZpcobVzyJqPtuwAAAAQKKcleAAABkN5hhLRV7imzm104lw");
        var response = client2.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        System.out.println(response);
    }
}