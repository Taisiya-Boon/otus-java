package ru.otus.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.otus.servlet.LoginServlet.START_PAGE_TEMPLATE;
import static ru.otus.servlet.UsersServlet.COOKIE_USER_ADMIN_NAME;
import static ru.otus.servlet.UsersServlet.COOKIE_USER_ID_NAME;

public class ExitServlet extends HttpServlet {

    public ExitServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cleanCookie(request, response);
        response.sendRedirect(START_PAGE_TEMPLATE);
    }

    public static void cleanCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_USER_ADMIN_NAME) ||
                        cookie.getName().equals(COOKIE_USER_ID_NAME)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

}
