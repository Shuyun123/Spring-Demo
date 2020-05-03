package net.anumbrella.spring.jwt.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;

import static net.anumbrella.spring.jwt.util.JwtUtil.USER_ID;

public class RequestWrapper extends HttpServletRequestWrapper {

    private String userId;

    RequestWrapper(HttpServletRequest request, String userId) {
        super(request);
        this.userId = userId;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (USER_ID.equals(name)) {
            return Collections.enumeration(Collections.singletonList(userId));
        }
        return super.getHeaders(name);
    }

    public String getUserId() {
        return userId;
    }

}
