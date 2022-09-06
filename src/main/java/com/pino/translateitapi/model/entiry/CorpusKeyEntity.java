package com.pino.translateitapi.model.entiry;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "corpus_key", schema = "public")
public class CorpusKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "project_oid")
    private Integer projectOid;

    @Column(name = "key")
    private String key;
}
