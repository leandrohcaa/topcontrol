package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface CaracteristicaProdutoRepository
		extends BaseRepository<CaracteristicaProduto, Long>, CaracteristicaProdutoRepositoryCustom {

	@Query(" select c from CaracteristicaProduto c join fetch c.grupoCaracteristicaProdutoList gc ")
	List<CaracteristicaProduto> findForCache();

	@Query(" select c from CaracteristicaProduto c join fetch c.requisicaoProdutoList rp where rp.id IN (:requisicaoIdList) ")
	List<CaracteristicaProduto> findByRequisicao(@Param("requisicaoIdList") List<Long> requisicaoIdList);
}
