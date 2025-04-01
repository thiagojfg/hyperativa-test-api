package com.hyperativa.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "cards")
public class CardEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", unique = true, nullable = false, updatable = false)
    private String cardNumber;
}
