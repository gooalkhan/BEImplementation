package com.example.beimplementation.Entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"baseDate", "baseTime", "nx", "ny", "category", "fcstDate", "fcstTime"})
})
public class VilageFcst {
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
    private String fcstTime;
    @Column
    private String fcstValue;
    @Column
    private LocalDateTime timestamp;
}
