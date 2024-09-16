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
import java.util.Map;
import java.util.Optional;

public class UsersServlet extends HttpServlet {
    private static final String LOGIN_PAGE_TEMPLATE = "/login";
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    public static final String TEMPLATE_ATTR_USER = "User";
    public static final String TEMPLATE_ATTR_USER_ADMIN = "adminFlag";
    public static final String COOKIE_USER_ADMIN_NAME = "userAdmin";
    public static final String COOKIE_USER_ID_NAME = "userId";

    private boolean loginFlag;

    private final DbServiceUserWeb dbServiceUserWeb;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DbServiceUserWeb dbServiceUserWeb) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUserWeb = dbServiceUserWeb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        boolean cookieFlag = false;
        boolean adminFlag = false;
        boolean cookieAttrAdminFlag = false;
        loginFlag = false;
        Long id = null;

        if (req.getCookies().length != 0) {
            for (Cookie cookie: req.getCookies()) {
                if (cookie.getName().equals(COOKIE_USER_ADMIN_NAME)) {
                    adminFlag = Boolean.parseBoolean(cookie.getValue());
                    cookieAttrAdminFlag = true;
                }
                if (cookie.getName().equals(COOKIE_USER_ID_NAME)) {
                    id = Long.parseLong(cookie.getValue());
                }
            }
            if (!cookieAttrAdminFlag && id != null) {
                cookieFlag = true;
            }
        }

        if (!cookieFlag) {
            try {
                String queryString = req.getQueryString();
                for (String query : queryString.split("&")) {
                    String[] queryPair = query.split("=");
                    if (queryPair[0].equals(TEMPLATE_ATTR_USER_ADMIN)) {
                        adminFlag = queryPair[1].equals("true");
                    }
                    if (queryPair[0].equals("userId")) {
                        id = Long.parseLong(queryPair[1]);
                    }
                }
            } catch (NullPointerException ex) {
                toLoginPage(response);
            }
        }

        if (!loginFlag) {
            paramsMap.put(TEMPLATE_ATTR_USER_ADMIN, adminFlag);
            try {
                Optional<User> user = dbServiceUserWeb.getUser(id);
                paramsMap.put(TEMPLATE_ATTR_USER, user.orElseThrow());
            } catch (NullPointerException ex) {
                toLoginPage(response);
            }

            if (!loginFlag) {
                if (!cookieFlag) {
                    response.addCookie(new Cookie(COOKIE_USER_ADMIN_NAME, String.valueOf(adminFlag)));
                    response.addCookie(new Cookie(COOKIE_USER_ID_NAME, String.valueOf(id)));
                }
                response.setContentType("text/html");
                response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
            }
        }
    }

    private void toLoginPage(HttpServletResponse response) throws IOException {
        loginFlag = true;
        response.sendRedirect(LOGIN_PAGE_TEMPLATE);
    }
}
