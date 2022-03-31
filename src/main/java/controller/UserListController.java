package controller;

import db.DataBase;
import model.ContentType;
import model.HttpStatus;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

public class UserListController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
            Collection<User> users = DataBase.findAll();
            response.setHttpStatus(HttpStatus.OK);
            response.setContentType(ContentType.HTML.getType());
            response.sendWithBody(writeHtmlWithUserData(users));
    }

    private byte[] writeHtmlWithUserData(Collection<User> users) throws IOException {
        String html =  new String(Files.readAllBytes(new File("./webapp/user/list.html").toPath()));
        StringBuilder result = new StringBuilder(html);
        int startIndex = html.indexOf("<tbody>") + 7;

        StringBuilder tableBody = new StringBuilder();
        for (User user : users) {
            tableBody.append("<tr><th>#</th> <th>" + user.getUserId() + "</th> <th>" + user.getName() + "</th> <th>" + user.getEmail() + "</th><th></th></tr>");
        }

        result.insert(startIndex, tableBody);
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }
}
