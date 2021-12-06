package cn.tojintao.contoller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author cjt
 * @date 2021/7/3 11:21
 * @description: 登录拦截器
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * 对未正常登陆的用户进行资源访问限制
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException, IOException {
        //强制转换
        HttpServletRequest request= (HttpServletRequest) req;
        //获取资源访问路径
        String uri=request.getRequestURI();
        //判断是否包含登陆资源相关的资源路径，要注意排除游客访问/css/js/图片/验证码等资源
        if(uri.contains("/index.jsp") || uri.contains("/user/login")||
                 uri.contains("/js/")||uri.contains("/css/")||uri.contains("/img/")){
            //包含，证明用户想登陆，放行
            chain.doFilter(req, resp);
        }
        else{
            //不包含，则需要通过获取Cookie验证用户是否已经登陆
            Cookie[] cookies=request.getCookies();
            //获取数据，遍历Cookies
            if(cookies!=null){
                for(Cookie cookie:cookies){
                    //如果有userId这个键，则证明登陆过了
                    if("userId".equals(cookie.getName())){
                        //放行
                        chain.doFilter(req, resp);
                    }
                }
            }
            else{
                request.getRequestDispatcher("/index.jsp").forward(request,resp);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
