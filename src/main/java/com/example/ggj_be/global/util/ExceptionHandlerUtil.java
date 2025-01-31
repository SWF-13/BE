package com.example.ggj_be.global.util;


import com.example.ggj_be.global.response.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.simple.JSONObject;

public class ExceptionHandlerUtil {

    public static void exceptionHandler(HttpServletResponse response, ErrorStatus errorStatus,
                                        int statusCode)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        JSONObject responseJson = new JSONObject();
        responseJson.put("isSuccess", "false");
        responseJson.put("message", errorStatus.getMessage());
        responseJson.put("code", errorStatus.getCode());

        response.getWriter().print(responseJson);

    }
}