package com.link.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "Follow")
@EqualsAndHashCode
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "follow_id")
    private int followID;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name="followee_id")
    private User followee;
}
