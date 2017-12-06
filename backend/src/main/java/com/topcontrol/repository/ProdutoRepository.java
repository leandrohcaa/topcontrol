package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface ProdutoRepository extends BaseRepository<Produto, Long>, ProdutoRepositoryCustom {

	@Query(" select p from Produto p where p.produtoPai is null ")
	List<Produto> findHierarchy();
}
