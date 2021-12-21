package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Map;

public interface PostRepository {
    Map<Long, Post> all();

    Post getById(long id);

    Post save(Post post);

    void removeById(long id);
}