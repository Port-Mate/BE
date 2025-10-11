package com.portmate.domain.berth.service;

import com.portmate.domain.berth.entity.Berth;
import com.portmate.domain.berth.repository.BerthRepository;
import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.entity.PortRegion;
import com.portmate.domain.pier.entity.ShipType;
import com.portmate.domain.pier.repository.PierRepository;
import com.portmate.domain.schedule.dto.response.AssignedShipResponse;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.repository.ScheduleRepository;
import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BerthAssignmentService {

	private final ScheduleRepository scheduleRepository;
	private final PierRepository pierRepository;
	private final BerthRepository berthRepository;
	private final BerthAssignmentStrategy assignmentStrategy;

	public List<AssignedShipResponse> assignByScheduleId(String scheduleId) {
		Schedule schedule = scheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new GlobalException(ErrorStatus.SCHEDULE_NOT_FOUND));

		List<Pier> piers = pierRepository.findAll();
		Map<String, List<Berth>> pierToBerths = berthRepository.findAll().stream()
			.collect(Collectors.groupingBy(Berth::getPierId));

		List<AssignedShipResponse> result = assignmentStrategy.assign(schedule.getScheduleContents(),
			schedule.getPier(),
			piers,
			pierToBerths);
		

		scheduleRepository.save(schedule);
		
		return result;
	}
}
