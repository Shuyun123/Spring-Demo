package net.anumbrella.spring.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT拦截的uri，黑白名单都配，优先黑名单
     */
    private String jwtFilterWhitelist = "/**";

    /**
     * JWT不拦截的uri，黑白名单都配，优先黑名单
     */
    private String jwtFilterBlacklist = "/user/login";
}
