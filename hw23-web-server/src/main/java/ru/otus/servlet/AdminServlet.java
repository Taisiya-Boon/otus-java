package ru.otus.servlet;

import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserWeb;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.otus.servlet.LoginServlet.START_PAGE_TEMPLATE;
import static ru.otus.servlet.UsersServlet.*;

public class AdminServlet extends HttpServlet {
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    public static final String USERS_PAGE_TEMPLATE = "/users";
    private static final String TEMPLATE_ATTR_USERS = "Users";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_ADMIN = "adminFlag";

    private final DbServiceUserWeb dbServiceUserWeb;
    private final TemplateProcessor templateProcessor;

    public AdminServlet(TemplateProcessor templateProcessor, DbServiceUserWeb dbServiceUserWeb) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUserWeb = dbServiceUserWeb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Cookie[] cookies = req.getCookies();
        boolean adminFlag = false;
        long id = 0L;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_USER_ADMIN_NAME)) {
                    adminFlag = Boolean.parseBoolean(cookie.getValue());
                }
                if (cookie.getName().equals(COOKIE_USER_ID_NAME)) {
                    id = Long.parseLong(cookie.getValue());
                }
            }
        }

        if (adminFlag) {
            getUsers(response);
        } else {
            if (id != 0L) {
                response.sendRedirect(USERS_PAGE_TEMPLATE);
            } else {
                response.sendRedirect(START_PAGE_TEMPLATE);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter(PARAM_NAME);
        String login = req.getParameter(PARAM_LOGIN);
        String password = req.getParameter(PARAM_PASSWORD);
        Boolean adminFlag = Boolean.parseBoolean(req.getParameter(PARAM_ADMIN));

        dbServiceUserWeb.saveUser(new User(0L, name, login, password, adminFlag));

        getUsers(response);
    }

    private void getUsers(HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<User> users = dbServiceUserWeb.getAllUsers();
        paramsMap.put(TEMPLATE_ATTR_USERS, users);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }
}
