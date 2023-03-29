package com.example.sportsbook_application_backend.model.entity;

import com.example.sportsbook_application_backend.model.enums.Outcome;
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
@Table(name = "Slips")
public class Slip {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Slip_SEQ")
    @SequenceGenerator(name = "Slip_SEQ")
    @Column(name = "id_slip")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//allows "foreign key on cascade delete"(deletes all slips when the user they were associated with is deleted)
    @JoinColumn(name = "user_id",referencedColumnName = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//allows "foreign key on cascade delete"(deletes all slips when the bet they were associated with is deleted)
    @JoinColumn(name = "bet_id",referencedColumnName = "id_bet", nullable = false)
    private Bet bet;

    @Column(name = "stake", nullable = false)
    private Float stake;

    @Column(name = "expected_profit", nullable = false)
    private Float expectedProfit;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private Outcome outcome;
}