package com.lotus.flatmate.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
