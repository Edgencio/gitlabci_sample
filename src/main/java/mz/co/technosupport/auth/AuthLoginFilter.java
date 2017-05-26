/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.auth;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mz.co.hi.web.ActiveUser;

/**
 *
 * @author Anselmo
 */
public class AuthLoginFilter implements Filter, Serializable {

    private String loginURL = "";

    @Override
    public void init(FilterConfig fc) throws ServletException {

        loginURL = fc.getInitParameter("login-url");

    }

    private String getPath(HttpServletRequest req) {

        String reqURI = req.getRequestURI();
        reqURI = reqURI.substring(1, reqURI.length());
        int slashIndex = reqURI.indexOf("/");
        return reqURI.substring(slashIndex, reqURI.length());

    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) sr;
        String path = getPath(req);

        ActiveUser activeUser = CDI.current().select(ActiveUser.class).get();
        Object auth = activeUser.getProperty("authorized");
        System.out.println(path);

        if (auth != null || path.equals(loginURL) || path.contains("webroot") || path.contains("hi-es5.js") || path.contains("f.m.call/UserFrontier/login") || path.equals("/")|| path.contains("api")) {
            fc.doFilter(sr, sr1); //good to go
            return;
        } else {
            HttpServletResponse resp = (HttpServletResponse) sr1;
//            resp.sendRedirect("entrance/login");
//            System.out.println(resp.toString());
            resp.sendError(403);
        }
    }

    @Override
    public void destroy() {

    }

}
