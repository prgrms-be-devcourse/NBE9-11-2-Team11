package com.back.team11.domain.cafe.repository;

import com.back.team11.domain.cafe.entity.Cafe;

import java.util.List;

public interface CafeRepositoryCustom {
    List<Cafe> searchCafes(CafeSearchCondition condition);
}
