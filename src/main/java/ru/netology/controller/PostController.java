package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    setResponseContentType(response);
    response.getWriter().print(gson.toJson(service.all()));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    setResponseContentType(response);
    response.getWriter().print(gson.toJson(service.getById(id)));
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    setResponseContentType(response);
    final var post = gson.fromJson(body, Post.class);
    response.getWriter().print(gson.toJson(service.save(post)));
  }

  public void removeById(long id, HttpServletResponse response) {
    setResponseContentType(response);
    service.removeById(id);
  }

  private void setResponseContentType(HttpServletResponse response){
    response.setContentType(APPLICATION_JSON);
  }
}
