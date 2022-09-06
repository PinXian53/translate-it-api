package com.pino.translateitapi.model.entiry;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "project", schema = "public")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date_time")
    private String createDateTime;
}
