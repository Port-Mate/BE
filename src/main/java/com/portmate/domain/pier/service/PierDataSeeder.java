package com.portmate.domain.pier.service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.entity.PortRegion;
import com.portmate.domain.pier.entity.ShipType;
import com.portmate.domain.pier.repository.PierRepository;

import lombok.RequiredArgsConstructor;

@Order(1)
@Component
@RequiredArgsConstructor
public class PierDataSeeder implements CommandLineRunner {

	private final PierRepository pierRepository;

	@Override
	public void run(String... args) {
		if (pierRepository.count() == 0) {

			// 여수항
			pierRepository.save(Pier.builder()
				.pierName("여수신항 제1부두")
				.region(PortRegion.YEOSU)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.BULK))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("여수신항 제2부두")
				.region(PortRegion.YEOSU)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("여수신항 제3부두")
				.region(PortRegion.YEOSU)
				.acceptedShipTypes(List.of(ShipType.SPECIAL, ShipType.GENERAL_CARGO, ShipType.CONTAINER))
				.build());

			// 광양항 (여천권)

			pierRepository.save(Pier.builder()
				.pierName("여천일반부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.CONTAINER))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("낙포부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.BULK, ShipType.OIL, ShipType.CHEMICAL, ShipType.GENERAL_CARGO))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("사포1부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.CHEMICAL, ShipType.OIL))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("사포2부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.CHEMICAL, ShipType.OIL))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("중흥부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.BULK, ShipType.OIL, ShipType.CHEMICAL))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("제2중흥부두")
				.region(PortRegion.GWANGYANG_YECHEON)
				.acceptedShipTypes(List.of(ShipType.BULK, ShipType.OIL, ShipType.CHEMICAL))
				.build());


			// 광양항 (광양권)
			pierRepository.save(Pier.builder()
				.pierName("하포일반부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.BULK))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("율촌일반부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.BULK))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("중마일반부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.BULK))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("제품부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.GENERAL_CARGO, ShipType.CHEMICAL, ShipType.SPECIAL))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("컨테이너부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.CONTAINER))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("자동차부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.RORO))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("시멘트부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.BULK))
				.build());

			pierRepository.save(Pier.builder()
				.pierName("태인부두")
				.region(PortRegion.GWANGYANG_MAIN)
				.acceptedShipTypes(List.of(ShipType.BULK, ShipType.SPECIAL))
				.build());
		}
	}
}
