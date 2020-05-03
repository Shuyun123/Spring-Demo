package net.anumbrella.spring.jwt.config;

import lombok.extern.slf4j.Slf4j;
import net.anumbrella.spring.jwt.util.JwtUtil;
import net.anumbrella.spring.jwt.util.ResponseUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    private final static ConcurrentMap<String, Boolean> CACHE_IS_FILTER_PATH = new ConcurrentHashMap<>();

    private final List<String> jwtFilterWhitelist;

    private final List<String> jwtFilterBlacklist;


    public JwtAuthenticationFilter(JwtProperties jwtProperties) {
        this.jwtFilterWhitelist = Arrays.stream(jwtProperties.getJwtFilterWhitelist().split(",")).map(String::trim).collect(toList());
        this.jwtFilterBlacklist = Arrays.stream(jwtProperties.getJwtFilterBlacklist().split(",")).map(String::trim).collect(toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isFilterUrl(httpServletRequest)) {
                // 获取请求头信息authorization信息
                final String authHeader = httpServletRequest.getHeader(JwtUtil.AUTH_HEADER_KEY);
                log.info("## authHeader = {}", authHeader);
                if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
                    ResponseUtil.renderResponse(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, "用户未登录，请先登录");
                    return;
                }


                // 获取token
                final String token = authHeader.substring(7);

                // 验证token
                if(!JwtUtil.validateToken(token)){
                    ResponseUtil.renderResponse(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, "token认证失败，请重新登录");
                }


                httpServletRequest = new RequestWrapper(httpServletRequest, JwtUtil.getUserId(token));
            }
        } catch (Exception e) {
            ResponseUtil.renderResponse(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, "登陆已经失效，请重新登录");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }



    public boolean isFilterUrl(HttpServletRequest request) {
        String uri = request.getServletPath();
        if (CACHE_IS_FILTER_PATH.containsKey(uri)) {
            return CACHE_IS_FILTER_PATH.get(uri);
        }
        boolean flag = isFilter(uri);
        CACHE_IS_FILTER_PATH.putIfAbsent(uri, flag);
        return flag;
    }


    private boolean isFilter(String uri) {
        boolean filter = true;
        for (String backRegex : jwtFilterBlacklist) {
            if (urlMatching(backRegex, uri)) {
                return false;
            }
        }
        for (String regex : jwtFilterWhitelist) {
            filter = urlMatching(regex, uri);
            if (filter) {
                return true;
            }
        }
        return filter;
    }

    protected boolean urlMatching(String regex, String uri) {
        return PATH_MATCHER.match(regex, uri);
    }
}
