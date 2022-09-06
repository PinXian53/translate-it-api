package com.pino.translateitapi.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "translation_key", schema = "public")
public class TranslationKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "project_oid")
    private Integer projectOid;

    @Column(name = "key")
    private String key;
}
