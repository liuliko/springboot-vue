package net.hnst.template.inteceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession session = request.getSession(true);


        if ("GET".equalsIgnoreCase(request.getMethod()) && !request.getRequestURI().contains("export")) {
            //直接拦截所有GET请求，返回index页面。所有的数据请求，必须使用POST
            response.setStatus(200);
            request.getRequestDispatcher("/vue/index.html").forward(request, response);
            return false;
        }

        /*
        if (session.getAttribute("user") == null) {
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.write("{ \"msg\" : 50014}");
            return false;
        }
        */

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
