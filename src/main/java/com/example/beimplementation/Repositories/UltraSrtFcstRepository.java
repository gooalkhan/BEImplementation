package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.UltraSrtFcst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UltraSrtFcstRepository extends JpaRepository<UltraSrtFcst, Integer> {
    Page<UltraSrtFcst> findAllByBaseDateAndBaseTimeAndNxAndNy(Pageable pageable, int baseDate, String baseTime, int nx, int ny);

    Integer countByBaseDateAndBaseTimeAndNxAndNy(int baseDate, String baseTime, int nx, int ny);
}
