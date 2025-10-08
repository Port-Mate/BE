package com.portmate.domain.berth.service;

import com.portmate.domain.berth.entity.Berth;
import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.entity.PortRegion;
import com.portmate.domain.pier.entity.ShipType;
import com.portmate.domain.schedule.dto.response.AssignedShipResponse;
import com.portmate.domain.schedule.util.DateParser;
import com.portmate.domain.schedule.vo.ScheduleContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BerthAssignmentStrategy {

	public List<AssignedShipResponse> assign(
		List<ScheduleContent> scheduleContents,
		String portName,
		List<Pier> availablePiers,
		Map<String, List<Berth>> pierToBerths
	) {
		List<AssignedShipResponse> result = new ArrayList<>();
		int vesselCount = scheduleContents.get(0).getData().size();

		Map<String, List<AssignmentSlot>> berthScheduleMap = new HashMap<>();


		List<Map<String, String>> vessels = new ArrayList<>();
		for (int i = 0; i < vesselCount; i++) {
			vessels.add(extractRow(scheduleContents, i));
		}
		vessels.sort(
			Comparator
				.comparing((Map<String, String> v) -> DateParser.parse(v.get("ETA (입항 예정)")))
				.thenComparing(v -> parseIMO(v.get("IMO / Call Sign")))
		);

		for (Map<String, String> row : vessels) {
			String vesselName = row.get("선박명 (Vessel Name)");
			String cargoType = row.get("화물 종류 (Cargo Type)");
			LocalDateTime eta = DateParser.parse(row.get("ETA (입항 예정)"));
			LocalDateTime etd = DateParser.parse(row.get("ETD (출항 예정)"));

		ShipType shipType = mapToShipType(cargoType);
		PortRegion region = mapToRegion(portName);


		List<Pier> eligiblePiers = availablePiers.stream()
			.filter(p -> p.getRegion() == region)
			.filter(p -> p.getAcceptedShipTypes().contains(shipType))
			.toList();

		Berth assignedBerth = null;
		Pier assignedPier = null;
		LocalDateTime finalEta = eta;
		LocalDateTime finalEtd = etd;


		AssignmentSlot bestSlot = null;
		Berth bestBerth = null;
		Pier bestPier = null;
		Duration minDelay = null;

		for (Pier pier : eligiblePiers) {
			List<Berth> candidateBerths = pierToBerths.getOrDefault(pier.getId(), List.of());

			for (Berth berth : candidateBerths) {
				List<AssignmentSlot> slots = berthScheduleMap.getOrDefault(berth.getId(), new ArrayList<>());

				AssignmentSlot adjustedSlot = adjustTimeSlot(slots, eta, etd);

				if (adjustedSlot != null) {
					Duration delay = Duration.between(eta, adjustedSlot.eta);

					if (minDelay == null || delay.compareTo(minDelay) < 0) {
						minDelay = delay;
						bestSlot = adjustedSlot;
						bestBerth = berth;
						bestPier = pier;
					}

					if (delay.isZero()) {
						break;
					}
				}
			}

			if (minDelay != null && minDelay.isZero()) {
				break;
			}
		}

		if (bestBerth != null && bestSlot != null) {
			assignedBerth = bestBerth;
			assignedPier = bestPier;
			finalEta = bestSlot.eta;
			finalEtd = bestSlot.etd;

			List<AssignmentSlot> slots = berthScheduleMap.getOrDefault(bestBerth.getId(), new ArrayList<>());
			slots.add(bestSlot);
			berthScheduleMap.put(bestBerth.getId(), slots);
		}

		result.add(new AssignedShipResponse(
			vesselName,
			cargoType,
			assignedPier != null ? assignedPier.getPierName() : "배정 불가",
			assignedBerth != null ? assignedBerth.getName() : "-",
			finalEta,
			finalEtd
		));
		}

		return result;
	}

	private AssignmentSlot adjustTimeSlot(List<AssignmentSlot> slots,
		LocalDateTime eta,
		LocalDateTime etd) {

		if (slots.isEmpty()) return new AssignmentSlot(eta, etd);

		slots.sort(Comparator.comparing(s -> s.eta));

		Duration duration = Duration.between(eta, etd);

		for (int i = 0; i <= slots.size(); i++) {
			if (i == slots.size()) {
				LocalDateTime newEta = slots.get(i - 1).etd;
				return new AssignmentSlot(newEta, newEta.plus(duration));
			}


			AssignmentSlot current = slots.get(i);
			LocalDateTime gapStart = (i == 0) ? eta : slots.get(i - 1).etd;
			LocalDateTime gapEnd = current.eta;

			if (gapStart.isAfter(etd)) {
				return new AssignmentSlot(gapStart, gapStart.plus(duration));
			}

			if (gapEnd.isAfter(gapStart) && Duration.between(gapStart, gapEnd).compareTo(duration) >= 0) {
				return new AssignmentSlot(gapStart, gapStart.plus(duration));
			}
		}

		return null;
	}

	private Map<String, String> extractRow(List<ScheduleContent> contents, int index) {
		return contents.stream()
			.collect(Collectors.toMap(
				ScheduleContent::getColumn,
				c -> c.getData().size() > index ? c.getData().get(index).trim() : ""
			));
	}

	private ShipType mapToShipType(String cargoType) {
		if (cargoType == null) return ShipType.GENERAL_CARGO;
		if (cargoType.contains("컨테이너")) return ShipType.CONTAINER;
		if (cargoType.contains("벌크") || cargoType.contains("곡물")) return ShipType.BULK;
		if (cargoType.contains("유조")) return ShipType.OIL;
		if (cargoType.contains("케미컬")) return ShipType.CHEMICAL;
		if (cargoType.contains("자동차")) return ShipType.RORO;
		return ShipType.GENERAL_CARGO;
	}

	private PortRegion mapToRegion(String portName) {
		if (portName.contains("여수")) return PortRegion.YEOSU;
		if (portName.contains("여천")) return PortRegion.GWANGYANG_YECHEON;
		return PortRegion.GWANGYANG_MAIN;
	}

	private static long parseIMO(String imoStr) {
		try {
			return Long.parseLong(imoStr.replaceAll("[^0-9]", ""));
		} catch (Exception e) {
			return Long.MAX_VALUE;
		}
	}

	private record AssignmentSlot(LocalDateTime eta, LocalDateTime etd) {}
}
