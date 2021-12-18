package ru.netology.repository;

import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepositoryImpl implements PostRepository {
    private final List<Post> allPosts;
    private final AtomicLong counterId;

    public PostRepositoryImpl() {
        this.allPosts = new CopyOnWriteArrayList<>();
        counterId = new AtomicLong(0);
    }

    public List<Post> all() {
        return allPosts;
    }

    public Optional<Post> getById(long id) {
        return allPosts.stream().filter(post -> post.getId() == id).findFirst();
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
        allPosts.removeIf(post -> post.getId() == id);
    }

    private Post saveNewPost(Post post) {
        post.setId(counterId.incrementAndGet());
        allPosts.add(post);
        return post;
    }
}
