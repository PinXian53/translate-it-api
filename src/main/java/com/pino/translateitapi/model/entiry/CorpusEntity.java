package com.pino.translateitapi.model.entiry;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "corpus", schema = "public")
public class CorpusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "corpus_key_oid")
    private String corpusKeyOid;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "translation")
    private String translation;
}
