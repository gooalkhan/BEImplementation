package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.VilageFcst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VilageFcstRepository extends JpaRepository<VilageFcst, Integer> {

    Page<VilageFcst> findAllByBaseDateAndBaseTimeAndNxAndNy(Pageable pageable, int baseDate, String baseTime, int nx, int ny);

    Integer countByBaseDateAndBaseTimeAndNxAndNy(int baseDate, String baseTime, int nx, int ny);
}
