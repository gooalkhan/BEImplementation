package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.UltraSrtFcst;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UltraSrtFcstRepository extends JpaRepository<UltraSrtFcst, Integer> {
    List<UltraSrtFcst> findAllByBaseDateAndBaseTimeAndNxAndNy(int baseDate, String baseTime, int nx, int ny);
}
