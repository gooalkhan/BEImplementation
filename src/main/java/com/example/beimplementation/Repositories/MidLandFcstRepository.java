package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.MidLandFcst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidLandFcstRepository extends JpaRepository<MidLandFcst, Integer> {
    List<MidLandFcst> findAllByRegIdAndTmFc(String regId, String tmFc);
}
