package com.example.user_test.filter;

import com.example.user_test.exception.UnAuthorizedReqeustException;
import com.example.user_test.util.jwt.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getJwtFromRequest(request);

        try {
            if (jwtToken != null && jwtTokenUtil.isJwtValid(jwtToken)) {
                // JWT가 유효한 경우, 사용자 정보를 설정
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (jwtToken == null) {
                throw new UnAuthorizedReqeustException("토큰정보가 없습니다.");
            } else if (!jwtTokenUtil.isJwtValid(jwtToken)) {
                throw new UnAuthorizedReqeustException("유효하지 않은 토큰정보입니다.");
            }

            // 다음 필터 체인으로 요청을 전달
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedReqeustException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(ex.getMessage());
            response.getWriter().flush();
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/jpa/users") ||
                path.equals("/jpa/login") ||
                path.equals("/jpa/public");
    }
}
