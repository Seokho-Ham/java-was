package controller;

import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static FrontController frontController = null;
    private static Map<String, Controller> guestPageControllers = new HashMap<>();
    private static Map<String, Controller> userPageControllers = new HashMap<>();

    static {
        guestPageControllers.put("/user/create", new UserCreateController());
        guestPageControllers.put("/user/login", new UserLoginController());
        userPageControllers.put("/user/list", new UserListController());
    }

    private FrontController() {
    }

    public static FrontController getInstance() {
        if (frontController == null) {
            frontController = new FrontController();
        }
        return frontController;
    }

    public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
        Controller controller;

        if (userPageControllers.containsKey(request.getPath())) {
            if (!isLogin(request)) {
                response.sendRedirect("/user/login.html");
            } else {
                controller = userPageControllers.get(request.getPath());
                controller.service(request, response);
            }
            return;
        }

        if (guestPageControllers.containsKey(request.getPath())) {
            controller = guestPageControllers.get(request.getPath());
        } else {
            controller = new DefaultController();
        }

        controller.service(request, response);

    }

    private boolean isLogin(HttpRequest request) {
        String cookie = request.getHeader().get("Cookie");
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookie);
        if (cookieMap.isEmpty() || cookieMap.get("sessionId").isEmpty()) {
            return false;
        }
        return true;
    }
}
