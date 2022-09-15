package com.pino.translateitapi.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "project_language")
public class ProjectLanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "project_oid")
    private Integer projectOid;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "progress_rate")
    private Integer progressRate;
}
