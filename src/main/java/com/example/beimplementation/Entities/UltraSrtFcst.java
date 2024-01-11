package com.example.beimplementation.Entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"baseDate", "baseTime", "nx", "ny", "category", "fcstDate", "fcstTime"})
})
public class UltraSrtFcst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer baseDate;
    @Column
    private String baseTime;
    @Column
    private Integer nx;
    @Column
    private Integer ny;
    @Column
    private String category;
    @Column
    private Integer fcstDate;
    @Column
    private Integer fcstTime;
    @Column
    private String fcstValue;
    @Column
    private LocalDateTime timestamp;
}
