package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepositoryImpl;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    public static String API_POSTS = "/api/posts";
    public static String API_POSTS_ID = "/api/posts/\\d+";

    @Override
    public void init() {
        final var repository = new PostRepositoryImpl();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            if (RequestTypes.GET.toString().equals(method)) {
                doGet(resp, path);
                return;
            }
            if (RequestTypes.POST.toString().equals(method)) {
                doPost(req, resp, path);
                return;
            }
            if (RequestTypes.DELETE.toString().equals(method)) {
                doDelete(resp, path);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void doGet(HttpServletResponse resp, String path) throws IOException {
        if (API_POSTS.equals(path)) {
            controller.all(resp);
            return;
        } else if (path.matches(API_POSTS_ID)) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private void doPost(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        if (API_POSTS.equals(path)) {
            controller.save(req.getReader(), resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private void doDelete(HttpServletResponse resp, String path) {
        if (path.matches(API_POSTS_ID)) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.removeById(id, resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private enum RequestTypes {
        GET, POST, DELETE
    }
}

