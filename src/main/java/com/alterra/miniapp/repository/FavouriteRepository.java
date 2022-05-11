package com.alterra.miniapp.repository;

import com.alterra.miniapp.domain.dao.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
}
