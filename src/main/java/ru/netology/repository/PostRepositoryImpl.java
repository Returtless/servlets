package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final Map<Long, Post> allPosts;
    private final AtomicLong counterId;

    public PostRepositoryImpl() {
        this.allPosts = new ConcurrentHashMap<>();
        counterId = new AtomicLong(0);
    }

    public Map<Long, Post> all() {
        return allPosts.entrySet().stream()
                .filter(x -> !x.getValue().isRemoved())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Post getById(long id) {
        Optional<Post> post = Optional.ofNullable(allPosts.get(id));
        if (post.isPresent()) {
            if (post.get().isRemoved()) {
                throw new NotFoundException("Пост удален");
            }
            return post.get();
        } else {
            throw new NotFoundException("Пост не существует");
        }
    }

    public Post save(Post post) {
        long postId = post.getId();

        if (postId == 0) {
            return saveNewPost(post);
        } else {
            Post editedPost = getById(postId);
            editedPost.setContent(post.getContent());
            return editedPost;
        }
    }

    public void removeById(long id) {
        allPosts.get(id).setRemoved();
    }

    private Post saveNewPost(Post post) {
        post.setId(counterId.incrementAndGet());
        allPosts.put(post.getId(), post);
        return post;
    }
}
