package com.lotus.flatmate.picture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.picture.entity.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>{

}
