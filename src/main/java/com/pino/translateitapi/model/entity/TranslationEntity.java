package com.pino.translateitapi.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "translation", schema = "public")
public class TranslationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "translation_key_oid")
    private Integer translationKeyOid;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "content")
    private String content;
}
