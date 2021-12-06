package cn.tojintao.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author cjt
 */
public class JsonUtil {
    /**
     * 读取客户端请求消息，返回json字符串给服务器
     * @param request
     * @return
     * @throws IOException
     */
    public static String getJsonString(HttpServletRequest request) throws IOException {
        //读取服务器的响应内容并显示
        InputStreamReader insr = new InputStreamReader(request.getInputStream(),"utf-8");
        //json为读取到的json字符串
        String json = "";
        int respInt = insr.read();
        while(respInt != -1){
            json += (char)respInt;
            respInt = insr.read();
        }
        //返回json字符串
        return json;
    }
}
