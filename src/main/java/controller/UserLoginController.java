package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class UserLoginController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getHttpMethod().equals("POST")) {
            Map<String, String> loginInfo = request.getParams();
            User user = DataBase.findUserById(loginInfo.get("userId"));

            if (user == null || !user.getPassword().equals(loginInfo.get("password"))) {
                response.sendRedirect("/user/login_failed.html");
            } else {
                response.setHeader("Set-Cookie", "sessionId=" + user.getUserId() + "; path=/");
                response.sendRedirect("/index.html");
            }
        } else {
            response.setHeader("Set-Cookie", "sessionId=; path=/; max-age=-1");
            response.sendRedirect("/index.html");
        }


    }
}
