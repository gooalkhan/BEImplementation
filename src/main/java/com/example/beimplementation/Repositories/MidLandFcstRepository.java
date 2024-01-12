package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.MidLandFcst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidLandFcstRepository extends JpaRepository<MidLandFcst, Integer> {
    //유니크 조건으로 검색 - 1건만 조회되어야함
    List<MidLandFcst> findAllByRegIdAndTmFc(String regId, String tmFc);
}
