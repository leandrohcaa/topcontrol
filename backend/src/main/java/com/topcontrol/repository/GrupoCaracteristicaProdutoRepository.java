package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface GrupoCaracteristicaProdutoRepository
		extends BaseRepository<GrupoCaracteristicaProduto, Long>, GrupoCaracteristicaProdutoRepositoryCustom {

	@Query(" select gp from GrupoCaracteristicaProduto gp ")
	List<GrupoCaracteristicaProduto> findForCache();
}
