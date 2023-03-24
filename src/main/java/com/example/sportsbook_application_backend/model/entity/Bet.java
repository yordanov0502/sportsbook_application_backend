package com.example.sportsbook_application_backend.model.entity;

import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Bet_SEQ")
    @SequenceGenerator(name = "Bet_SEQ")
    @Column(name = "id_bet")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//allows "foreign key on cascade delete"(deletes all bets when the event they were associated with is deleted)
    @JoinColumn(name = "event_id",referencedColumnName = "id_event", nullable = false)
    private Event event;

    @Column(name = "outcome")
    @Enumerated(EnumType.STRING)
    private Outcome outcome;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResultType type;

    @Column(name = "odd", nullable = false)
    private Float odd;

}