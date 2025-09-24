package com.portmate.domain.pier.entity;

//Todo: 추후에 ship 패키지로 옮길거
public enum ShipType {
	CONTAINER,     // 컨테이너선
	RORO,          // 자동차선
	BULK,          // 벌크선 (석탄, 곡물, 시멘트 등)
	OIL,           // 유조선
	CHEMICAL,      // 케미컬/석유화학
	GENERAL_CARGO, // 잡화선, 다목적선
	SPECIAL        // 특수 화물선
}
