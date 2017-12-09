package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface GrupoProdutoRepository extends BaseRepository<GrupoProduto, Long>, GrupoProdutoRepositoryCustom {

	@Query(" select gp from GrupoProduto gp join fetch gp.usuarioNegocioList un ")
	List<GrupoProduto> findForCache();

	@Query(" select p from Produto p join fetch p.grupoProdutoList gp ")
	List<Produto> findProdutoForCache();
}
