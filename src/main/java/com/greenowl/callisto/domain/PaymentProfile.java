package com.greenowl.callisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A payment profile.
 */
@Entity
@Table(name = "T_CUSTOMER_PAYMENT_PROFILE")
public class PaymentProfile extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "card_token", nullable = false)
    private String cardToken;

    @Column(name = "exp_month", nullable = false)
    private Long expMonth;

    @Column(name = "exp_year", nullable = false)
    private Long expYear;

    @Column(name = "last4", nullable = false)
    private String last4;
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User profileHolder;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentProfile", targetEntity = PlanSubscription.class, fetch = FetchType.LAZY)
    private Set<PlanSubscription> planSubscriptions = new HashSet<>();

    public PaymentProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }


    public Long getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Long expMonth) {
        this.expMonth = expMonth;
    }

    public Long getExpYear() {
        return expYear;
    }

    public void setExpYear(Long expYear) {
        this.expYear = expYear;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getProfileHolder() {
        return profileHolder;
    }

    public void setProfileHolder(User profileHolder) {
        this.profileHolder = profileHolder;
    }

    public Set<PlanSubscription> getPlanSubscriptions() {
        return planSubscriptions;
    }

    public void setPlanSubscriptions(Set<PlanSubscription> planSubscriptions) {
        this.planSubscriptions = planSubscriptions;
    }


}	

