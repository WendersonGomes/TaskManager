package com.esig.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 50, message = "O título não pode ultrapassar 50 caracteres")
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição não pode ultrapassar 255 caracteres")
    private String description;

    @NotBlank(message = "O responsável é obrigatório")
    private String responsible;

    @NotNull(message = "O prazo é obrigatório")
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
