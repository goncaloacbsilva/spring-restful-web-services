package com.goncaloacbs.rest.webservices.restfulwebservices.user;

import com.goncaloacbs.rest.webservices.restfulwebservices.post.Post;
import com.goncaloacbs.rest.webservices.restfulwebservices.post.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJPAResource {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/users")
    public List<EntityModel<User>> retrieveAllUsers() {

        List<EntityModel<User>> model = userRepository.findAll()
                .stream()
                .map(user -> EntityModel.of(user, linkTo(methodOn(this.getClass()).retrieveUser(user.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return model;
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        EntityModel<User> model = EntityModel.of(user.get());

        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        model.add(linkToUsers.withRel("all-users"));

        return model;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }



    @GetMapping("/users/{id}/posts")
    public List<EntityModel<Post>> retrieveAllUserPosts(@PathVariable int id) {

        // Get user

        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        List<EntityModel<Post>> model = user.get().getPosts()
                .stream()
                .map(post -> EntityModel.of(post, linkTo(methodOn(this.getClass()).retrievePost(post.getUser().getId(), post.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return model;
    }


    @GetMapping("/users/{id}/posts/{postId}")
    public EntityModel<Post> retrievePost(@PathVariable int id, @PathVariable int postId) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("id-" + id);
        }

        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()) {
            throw new PostNotFoundException("postId-" + postId);
        }

        EntityModel<Post> model = EntityModel.of(post.get());

        model.add(linkTo(methodOn(this.getClass()).retrieveAllUserPosts(post.get().getUser().getId())).withRel("userPosts"));

        return model;
    }



    @PostMapping("/users/{id}/posts")
    public ResponseEntity createPost(@PathVariable int id, @RequestBody Post post) {

        // Get user

        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        User user = userOptional.get();

        post.setUser(user);

        postRepository.save(post);

        URI postLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{postId}").buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(postLocation).build();
    }


}
