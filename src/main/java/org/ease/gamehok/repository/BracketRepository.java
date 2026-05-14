package org.ease.gamehok.repository;

import org.ease.gamehok.entity.Bracket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface BracketRepository extends JpaRepository<Bracket,Long>{
}
