package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.*;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.entity.ScheduleVersion;
import com.portmate.domain.schedule.repository.ScheduleRepository;
import com.portmate.domain.schedule.repository.ScheduleVersionRepository;
import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.domain.schedule.util.DateParser;
import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.entity.ShipType;
import com.portmate.domain.pier.repository.PierRepository;

import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;
import com.portmate.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleVersionService {
    
    private final ScheduleVersionRepository versionRepository;
    private final ScheduleRepository scheduleRepository;
    private final PierRepository pierRepository;
    

    public ScheduleValidateResponse validateScheduleModification(ScheduleValidateRequest request) {
        List<ScheduleValidateResponse.ValidationError> errors = new ArrayList<>();

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new GlobalException(ErrorStatus.SCHEDULE_NOT_FOUND));

        Map<String, ScheduleContent> shipMap = schedule.getScheduleContents().stream()
                .collect(Collectors.toMap(ScheduleContent::getVesselName, sc -> sc));
        

        for (ShipPlacement placement : request.getPlacements()) {
            ScheduleContent ship = shipMap.get(placement.getVesselName());
            if (ship == null) {
                errors.add(ScheduleValidateResponse.ValidationError.builder()
                        .shipId(placement.getVesselName())
                        .errorType("SHIP_NOT_FOUND")
                        .message("선박을 찾을 수 없습니다")
                        .build());
                continue;
            }

            if (!isPierCompatible(placement.getPier(), ship.getCargoType())) {
                errors.add(ScheduleValidateResponse.ValidationError.builder()
                        .shipId(placement.getVesselName())
                        .errorType("CARGO_TYPE_MISMATCH")
                        .message(String.format("%s: 화물 타입 '%s'는 %s에 접안할 수 없습니다",
                                ship.getVesselName(), ship.getCargoType(), placement.getPier()))
                        .build());
            }

            
        }
        for (int i = 0; i < request.getPlacements().size(); i++) {
            ShipPlacement p1 = request.getPlacements().get(i);
            LocalDateTime eta1 = DateParser.parse(p1.getEta());
            LocalDateTime etd1 = DateParser.parse(p1.getEtd());
            
            for (int j = i + 1; j < request.getPlacements().size(); j++) {
                ShipPlacement p2 = request.getPlacements().get(j);

                if (p1.getPier().equals(p2.getPier()) && p1.getBerth().equals(p2.getBerth())) {
                    LocalDateTime eta2 = DateParser.parse(p2.getEta());
                    LocalDateTime etd2 = DateParser.parse(p2.getEtd());

                    if (timesOverlap(eta1, etd1, eta2, etd2)) {
                        ScheduleContent ship1 = shipMap.get(p1.getVesselName());
                        ScheduleContent ship2 = shipMap.get(p2.getVesselName());
                        
                        errors.add(ScheduleValidateResponse.ValidationError.builder()
                                .shipId(p1.getVesselName())
                                .errorType("TIME_CONFLICT")
                                .message(String.format("%s: %s %s에서 %s와 시간이 겹칩니다",
                                        ship1 != null ? ship1.getVesselName() : "Unknown",
                                        p1.getPier(), p1.getBerth(),
                                        ship2 != null ? ship2.getVesselName() : "Unknown"))
                                .build());
                    }
                }
            }
        }

        if (request.getPlacements().size() != schedule.getScheduleContents().size()) {
            errors.add(ScheduleValidateResponse.ValidationError.builder()
                    .shipId(null)
                    .errorType("INCOMPLETE_PLACEMENT")
                    .message(String.format("모든 선박을 배치해야 합니다. (배치됨: %d, 전체: %d)",
                            request.getPlacements().size(), schedule.getScheduleContents().size()))
                    .build());
        }
        
        return ScheduleValidateResponse.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();
    }
    

    @Transactional
    public ScheduleModifyResponse saveScheduleModification(String userId, String userName, ScheduleModifyRequest request) {

        ScheduleValidateRequest validateRequest = new ScheduleValidateRequest(
                request.getScheduleId(),
                request.getPlacements()
        );
        ScheduleValidateResponse validation = validateScheduleModification(validateRequest);
        
        if (!validation.isValid()) {
            throw new GlobalException(ErrorStatus.INVALID_BERTH_PLACEMENT);
        }

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new GlobalException(ErrorStatus.SCHEDULE_NOT_FOUND));

        List<ScheduleContent> updatedContents = createUpdatedContents(schedule, request.getPlacements());

        ScheduleVersion version = ScheduleVersion.builder()
                .versionId(UUID.randomUUID().toString())
                .originalScheduleId(schedule.getScheduleId())
                .pier(schedule.getPier())
                .berth(schedule.getBerth())
                .startDt(schedule.getStartDt())
                .endDt(schedule.getEndDt())
                .scheduleContents(updatedContents)
                .status(ScheduleVersion.VersionStatus.PENDING) // 승인 대기
                .createdBy(userId)
                .createdByName(userName)
                .comment(request.getComment())
                .build();
        
        ScheduleVersion saved = versionRepository.save(version);
        
        return ScheduleModifyResponse.builder()
                .versionId(saved.getVersionId())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }
    

    public ScheduleVersionResponse getScheduleVersion(String versionId) {
        ScheduleVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.SCHEDULE_NOT_FOUND));
        
        return ScheduleVersionResponse.builder()
                .versionId(version.getVersionId())
                .originalScheduleId(version.getOriginalScheduleId())
                .pier(version.getPier())
                .berth(version.getBerth())
                .startDt(version.getStartDt())
                .endDt(version.getEndDt())
                .status(version.getStatus())
                .createdAt(version.getCreatedAt())
                .scheduleContents(version.getScheduleContents())
                .build();
    }

    public PageResponse<List<ScheduleVersionResponse>> getAllVersions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ScheduleVersion> versionPage = versionRepository.findAll(pageable);
        
        List<ScheduleVersionResponse> content = versionPage.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        
        return PageResponse.of(
                content,
                versionPage.getNumber(),
                versionPage.getSize(),
                versionPage.getTotalElements(),
                versionPage.getTotalPages()
        );
    }

    private ScheduleVersionResponse toResponse(ScheduleVersion version) {
        return ScheduleVersionResponse.builder()
                .versionId(version.getVersionId())
                .originalScheduleId(version.getOriginalScheduleId())
                .pier(version.getPier())
                .berth(version.getBerth())
                .startDt(version.getStartDt())
                .endDt(version.getEndDt())
                .status(version.getStatus())
                .createdBy(version.getCreatedBy())
                .createdByName(version.getCreatedByName())
                .comment(version.getComment())
                .createdAt(version.getCreatedAt())
                .scheduleContents(version.getScheduleContents())
                .build();
    }
    

    private List<ScheduleContent> createUpdatedContents(Schedule schedule, List<ShipPlacement> placements) {

        Map<String, ShipPlacement> placementMap = placements.stream()
                .collect(Collectors.toMap(ShipPlacement::getVesselName, p -> p));
        
        List<ScheduleContent> updatedContents = new ArrayList<>();
        
        for (ScheduleContent originalContent : schedule.getScheduleContents()) {
            ShipPlacement placement = placementMap.get(originalContent.getVesselName());
            
            if (placement != null) {
                ScheduleContent updatedContent = ScheduleContent.builder()
                        .id(originalContent.getId())
                        .no(originalContent.getNo())
                        .vesselName(originalContent.getVesselName())
                        .imoOrCallSign(originalContent.getImoOrCallSign())
                        .voyageNo(originalContent.getVoyageNo())
                        .eta(placement.getEta())
                        .etd(placement.getEtd())
                        .fromPort(originalContent.getFromPort())
                        .nextPort(originalContent.getNextPort())
                        .cargoType(originalContent.getCargoType())
                        .tonnage(originalContent.getTonnage())
                        .flag(originalContent.getFlag())
                        .remark(originalContent.getRemark())
                        .pier(placement.getPier())
                        .berth(placement.getBerth())
                        .build();
                updatedContents.add(updatedContent);
            } else {
                updatedContents.add(originalContent);
            }
        }
        
        return updatedContents;
    }
    

    private boolean timesOverlap(LocalDateTime start1, LocalDateTime end1, 
                                  LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    

    private boolean isPierCompatible(String pierName, String cargoType) {
        if (cargoType == null) return true;
        

        Pier pier = pierRepository.findAll().stream()
                .filter(p -> p.getPierName().equals(pierName))
                .findFirst()
                .orElse(null);
        
        if (pier == null) return true;

        ShipType shipType = mapToShipType(cargoType);

        return pier.getAcceptedShipTypes().contains(shipType);
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
    

    private String getPierIdByName(String pierName) {
        return pierRepository.findAll().stream()
                .filter(p -> p.getPierName().equals(pierName))
                .map(Pier::getId)
                .findFirst()
                .orElse(null);
    }
}
