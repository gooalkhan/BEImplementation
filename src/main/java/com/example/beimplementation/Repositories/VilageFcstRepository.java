package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.VilageFcst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VilageFcstRepository extends JpaRepository<VilageFcst, Integer> {
    //페이징처리, 동일지역에 동일시각에 발표된 예보는 여러개일 수 있음
    Page<VilageFcst> findAllByBaseDateAndBaseTimeAndNxAndNy(Pageable pageable, int baseDate, String baseTime, int nx, int ny);

    Integer countByBaseDateAndBaseTimeAndNxAndNy(int baseDate, String baseTime, int nx, int ny);
}
