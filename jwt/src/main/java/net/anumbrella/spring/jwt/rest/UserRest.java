package net.anumbrella.spring.jwt.rest;

import com.google.gson.Gson;
import net.anumbrella.spring.jwt.model.UserDto;
import net.anumbrella.spring.jwt.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

import static net.anumbrella.spring.jwt.util.JwtUtil.USER_ID;

@RestController
@RequestMapping(value = "user")
public class UserRest {


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated({PostMapping.class, Default.class}) UserDto userDto,
                                HttpServletResponse response) {
        // 获取用户ID
        Long userId = 1L;
        Gson gson = new Gson();
        String token = JwtUtil.generateToken(String.valueOf(userId), gson.toJson(userDto));
        // 将token放在响应头
        response.setHeader(JwtUtil.AUTH_HEADER_KEY, JwtUtil.TOKEN_PREFIX + token);
        return ResponseEntity.ok("login success");
    }


    @GetMapping("/auth-info")
    public ResponseEntity authInfo(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtUtil.AUTH_HEADER_KEY);
        String token = authHeader.substring(7);
        return ResponseEntity.ok(JwtUtil.getUserId(token));
    }

    @GetMapping("/test")
    public ResponseEntity test(@RequestHeader(value = USER_ID) Long userId) {
        System.err.println(userId);
        return ResponseEntity.ok(userId);
    }

}
