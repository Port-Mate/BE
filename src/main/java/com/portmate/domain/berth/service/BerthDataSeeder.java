package com.portmate.domain.berth.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.portmate.domain.berth.entity.Berth;
import com.portmate.domain.berth.repository.BerthRepository;
import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.repository.PierRepository;
import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Component
@Order(2)
@RequiredArgsConstructor
public class BerthDataSeeder implements CommandLineRunner {

	private final PierRepository pierRepository;
	private final BerthRepository berthRepository;

	@Override
	public void run(String... args) {
		if (berthRepository.count() == 0) {
			createBerths("여수신항 제1부두", 3);
			createBerths("여수신항 제2부두", 1);
			createBerths("여수신항 제3부두", 1);

			createBerths("여천일반부두", 1);
			createBerths("낙포부두", 1);
			createBerths("사포1부두", 1);
			createBerths("사포2부두", 1);
			createBerths("중흥부두", 1);
			createBerths("제2중흥부두", 1);

			createBerths("하포일반부두", 3);
			createBerths("율촌일반부두", 1);
			createBerths("중마일반부두", 2);
			createBerths("제품부두", 11);
			createBerths("컨테이너부두", 12);
			createBerths("자동차부두", 4);
			createBerths("시멘트부두", 6);
			createBerths("태인부두", 1);
		}
	}

	private void createBerths(String pierName, int berthCount) {
		Pier pier = pierRepository.findByPierName(pierName)
			.orElseThrow(() -> new GlobalException(ErrorStatus.BAD_REQUEST));
		if (pier != null) {
			for (int i = 1; i <= berthCount; i++) {
				berthRepository.save(Berth.builder()
					.berthId(String.valueOf(i))
					.name("제" + i + "선석")
					.pierId(pier.getId())
					.build());
			}
		}
	}
}
