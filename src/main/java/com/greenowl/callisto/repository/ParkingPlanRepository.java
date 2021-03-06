package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.ParkingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingPlanRepository extends JpaRepository<ParkingPlan, Long> {

    @Query("select u from ParkingPlan u where u.planName = ?1")
    ParkingPlan getOneParkingPlanByPlanName(String planName);

    @Query("select u from ParkingPlan u where u.active = true")
    List<ParkingPlan> getActivedParkingPlans();

    @Query("select u from ParkingPlan u where u.planTerminatedDays = 0")
    List<ParkingPlan> getRecurringParkingPlans();

    @Query("select u from ParkingPlan u where u.id=?1")
    ParkingPlan getOneParkingPlanById(Long id);
}
