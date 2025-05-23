package com.esig.taskmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String responsible;
    private LocalDate deadLine;

    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Priority {
        LOW, MEDIUM, HIGH;

        public String getLabelInPTBR() {
            switch (this) {
                case LOW:
                    return "Baixa";
                case MEDIUM:
                    return "Média";
                case HIGH:
                    return "Alta";
                default:
                    return "Desconhecido";
            }
        }
    }


    public enum Status {
        PROGRESS, COMPLETE;

        public String getLabelInPTBR() {
            switch (this) {
                case PROGRESS:
                    return "Em andamento";
                case COMPLETE:
                    return "Concluída";
                default:
                    return "Desconhecido";
            }
        }
    }
}
