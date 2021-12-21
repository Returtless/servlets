package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepositoryImpl implements PostRepository {
    private final Map<Long, Post> allPosts;
    private final AtomicLong counterId;

    public PostRepositoryImpl() {
        this.allPosts = new ConcurrentHashMap<>();
        counterId = new AtomicLong(0);
    }

    public Map<Long, Post> all() {
        return allPosts;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(allPosts.get(id));
    }

    public Post save(Post post) {
        long postId = post.getId();

        if (postId == 0) {
            return saveNewPost(post);
        } else {
            Optional<Post> optionalEditedPost = getById(postId);
            if (optionalEditedPost.isPresent()) {
                Post editedPost = optionalEditedPost.get();
                editedPost.setContent(post.getContent());
                return editedPost;
            } else {
                return saveNewPost(post);
            }
        }
    }

    public void removeById(long id) {
        allPosts.remove(id);
    }

    private Post saveNewPost(Post post) {
        post.setId(counterId.incrementAndGet());
        allPosts.put(post.getId(), post);
        return post;
    }
}
