package com.pino.translateitapi.model.entiry;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "language", schema = "public")
public class LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
}
