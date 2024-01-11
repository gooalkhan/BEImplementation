package com.example.beimplementation.Entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"tmFc", "stnId"})
})
public class MidFcst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer numOfRows;

    @Column
    private Integer pageNo;

    @Column
    private Integer totalCount;

    @Column
    private Integer resultCode;

    @Column(length = 100)
    private String resultMsg;

    @Column
    private String dataType;

    @Column(length=1000)
    private String wfSv;

    @Column
    private String tmFc;

    @Column
    private Integer stnId;

    @Column
    private LocalDateTime timeStamp;
}
