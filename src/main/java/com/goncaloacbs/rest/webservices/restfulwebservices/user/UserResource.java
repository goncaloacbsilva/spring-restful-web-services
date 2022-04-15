package com.goncaloacbs.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
public class UserResource {
    @Autowired
    private UserDaoService service;

    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException("id-"+ id);
        }

        EntityModel<User> model = EntityModel.of(user);

        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        model.add(linkToUsers.withRel("all-users"));

        return model;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteUserById(id);

        if (user == null) {
            throw new UserNotFoundException("id-" + id);
        }

    }

    @GetMapping("/users/{id}/posts")
    public Collection<Post> retrieveAllUserPosts(@PathVariable int id) {
        return this.retrieveUser(id).getContent().getPosts();
    }

    @GetMapping("/users/{id}/posts/{postId}")
    public Post retrieveUserPost(@PathVariable int id, @PathVariable int postId) {
        Post post = this.retrieveUser(id).getContent().getPost(postId);

        if (post == null) {
            throw new PostNotFoundException("postId-" + postId);
        }

        return post;
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity createPost(@PathVariable int id, @RequestBody Post post) {
        Post savedPost = this.retrieveUser(id).getContent().createPost(post);

        URI postLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{postId}").buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity.created(postLocation).build();
    }

}
