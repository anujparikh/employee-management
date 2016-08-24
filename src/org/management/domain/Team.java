package org.management.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private List<Employee> memberList;
    @NotNull
    private Manager manager;
    @NotNull
    private String hrId;
    private Employee teamAnchor;

    public Team(String id, String name, List<Employee> memberList, Manager manager, String hrId, Employee teamAnchor) {
        this.id = id;
        this.name = name;
        this.memberList = memberList;
        this.manager = manager;
        this.hrId = hrId;
        this.teamAnchor = teamAnchor;
    }
}
