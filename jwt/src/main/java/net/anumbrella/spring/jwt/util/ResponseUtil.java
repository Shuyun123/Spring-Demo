package net.anumbrella.spring.jwt.util;


import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anumbrella
 */
public class ResponseUtil {

    private static final Map<String, Object> RESULT = new HashMap<>();
    private static final Gson JsonUtil = new Gson();

    public static void renderResponse(HttpServletResponse response, int code, String tip) throws IOException {
        RESULT.put("code", code);
        RESULT.put("data", tip);
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.toJson(RESULT));
    }
}
