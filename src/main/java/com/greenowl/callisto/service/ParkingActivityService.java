package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.ParkingActivityFactory;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.util.PaginationUtil;
import com.greenowl.callisto.util.ParkingActivityUtil;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingActivityService.class);

    @Inject
    private ParkingActivityRepository parkingActivityRepository;

    private List<ParkingActivity> findAllActivityBetween(DateTime startTime, DateTime endTime) {
        LOG.debug("Looking for records between startTime = {} and endTime = {}", startTime, endTime);
        return parkingActivityRepository.getParkingActivityBetween(startTime, endTime);
    }

    private List<ParkingActivity> findAllActivityByTypeBetween(DateTime startTime, DateTime endTime, String type) {
        LOG.debug("Looking for records between startTime = {} and endTime = {}", startTime, endTime);
        return parkingActivityRepository.getParkingActivityByTypeBetween(startTime, endTime, type);
    }


    /**
     * Create parking activity for plan user and store in the database.
     *
     * @param user       the user this plan belongs to.
     * @param plan       the parking plan.
     * @param deviceInfo user's device that he/she is using.
     * @return
     */
    public ParkingActivityDTO createParkingActivityForPlanUser(User user, ParkingPlan plan, String deviceInfo) {
        ParkingActivity newActivity = ParkingActivityFactory.create(user, plan, Constants.PARKING_STATUS_PARKING_START, deviceInfo);
        parkingActivityRepository.save(newActivity);
        return ParkingActivityUtil.constructDTO(newActivity, user);
    }

    /**
     * Returning the latest in flight parking activity for a particular user.
     *
     * @param user the User this parking activity should belong to.
     * @return
     */
    public Optional<ParkingActivity> getInFlightRecordForUser(User user) {
        return findByStatusAndUser(user, Constants.PARKING_STATUS_IN_FLIGHT);
    }

    private Optional<ParkingActivity> findByStatusAndUser(User user, String status) {
        ParkingActivity activity = parkingActivityRepository.getParkingActivityByUserAndStatus(user, status);
        return (activity == null) ? Optional.empty() : Optional.of(activity);
    }

    /**
     * Return all the activities between start date and end date based on the
     * filtered type.
     *
     * @return
     */
    public List<ParkingActivity> findAllActivitiesBetweenStartAndEndDates(DateTime start, DateTime end) {
        return findAllActivityBetween(start, end);
    }

    public List<ParkingActivity> findAllActivitiesBetweenStartAndEndDates(DateTime start, DateTime end, String type) {
        return findAllActivityByTypeBetween(start, end, type);
    }

    public void updateParkingStatus(String parkingStatus, long id) {
        parkingActivityRepository.setParkingStatusById(parkingStatus, id);
    }

    public void updateGateResponse(String gateResponse, long id) {
        parkingActivityRepository.setGateResponse(gateResponse, id);
    }

    public void updateExitTime(DateTime timestamp, long id) {
        parkingActivityRepository.setExitTime(timestamp, id);
    }

    public Optional<ParkingActivity> getLatestActivityForUser(User user) {
        Page<ParkingActivity> page = parkingActivityRepository.findByActivityHolder(user, PaginationUtil.generateDescSortedIdrequest(0, 5));
        List<ParkingActivity> content = page.getContent();
        if (content != null && !content.isEmpty()) {
            content.get(0).getActivityHolder();
            return Optional.of(content.get(0));
        }
        return Optional.empty();
    }

    public void updateExceptionFlag(String exceptionFlag, long id) {
        parkingActivityRepository.setExceptionFlag(exceptionFlag, id);
    }

    public void save(ParkingActivity parkingActivity){
    	parkingActivityRepository.save(parkingActivity);
    }
    
    public ParkingActivity findById(Long id){
    	return parkingActivityRepository.getParkingActivityById(id);
    }
}
