package com.example.novels.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novels.novel.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
