package ru.otus.servlet;

import javafx.util.Pair;
import ru.otus.core.model.User;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static ru.otus.servlet.ExitServlet.cleanCookie;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    public static final String START_PAGE_TEMPLATE = "/";

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cleanCookie(request, response);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cleanCookie(request, response);

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        Pair<Boolean, User> authInfo = userAuthService.authenticate(name, password);

        if (Boolean.TRUE.equals(authInfo.getKey())) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/users?adminFlag="+authInfo.getValue().getAdminFlag()
                    +"&userId="+authInfo.getValue().getId());
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }

    }

}

