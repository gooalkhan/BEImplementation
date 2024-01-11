package com.example.beimplementation.Entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"tmFc", "regId"})
})
public class MidLandFcst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="regId")
    private String regId;

    @Column(name="tmFc")
    private String tmFc;

    @Column
    private Integer rnSt3Am;

    @Column
    private Integer rnSt3Pm;

    @Column
    private Integer rnSt4Am;

    @Column
    private Integer rnSt4Pm;

    @Column
    private Integer rnSt5Am;

    @Column
    private Integer rnSt5Pm;

    @Column
    private Integer rnSt6Am;

    @Column
    private Integer rnSt6Pm;

    @Column
    private Integer rnSt7Am;

    @Column
    private Integer rnSt7Pm;

    @Column
    private Integer rnSt8;

    @Column
    private Integer rnSt9;

    @Column
    private Integer rnSt10;

    @Column
    private String wf3Am;

    @Column
    private String wf3Pm;

    @Column
    private String wf4Am;

    @Column
    private String wf4Pm;
    @Column
    private String wf5Am;
    @Column
    private String wf5Pm;
    @Column
    private String wf6Am;

    @Column
    private String wf6Pm;

    @Column
    private String wf7Am;

    @Column
    private String wf7Pm;

    @Column
    private String wf8;

    @Column
    private String wf9;

    @Column
    private String wf10;

    @Column
    private LocalDateTime timestamp;
}
