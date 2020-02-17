package application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "debit_account", cascade=CascadeType.ALL, orphanRemoval = true)
    private transient Set<Transfer> debit;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "credit_account", cascade=CascadeType.ALL, orphanRemoval = true)
    private transient Set<Transfer> account;

    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    @Column(name = "blocked_amount")
    private BigDecimal blockedAmount;
}
