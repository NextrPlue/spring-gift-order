package gift.interceptor;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
                request.setAttribute("claims", claims);
                return true;
            } catch (Exception e) {
                response.sendRedirect("/members/login");
                return false;
            }
        }
        response.sendRedirect("/members/login");
        return false;
    }
}
