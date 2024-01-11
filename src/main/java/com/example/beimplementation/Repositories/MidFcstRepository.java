package com.example.beimplementation.Repositories;

import com.example.beimplementation.Entities.MidFcst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidFcstRepository extends JpaRepository<MidFcst, Integer> {

    MidFcst findFirstByOrderByIdDesc();

    List<MidFcst> findAllByNumOfRowsAndPageNoAndDataTypeAndStnIdAndTmFc(int numOfRows, int pageNo, String dataType,  int stnId, String tmFc);
}
