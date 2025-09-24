package com.portmate.domain.pier.repository.custom;

import java.util.Optional;

import com.portmate.domain.pier.entity.Pier;

public interface CustomPierRepository
{
	Optional<Pier> findByPierName(String pierName);
}
