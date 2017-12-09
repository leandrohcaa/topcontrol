package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface RequisicaoProdutoRepository
		extends BaseRepository<RequisicaoProduto, Long>, RequisicaoProdutoRepositoryCustom {

	@Query(" select rp from RequisicaoProduto rp JOIN FETCH rp.requisicao r JOIN FETCH rp.produto p "
			+ " where r.usuario.id = :usuarioId " + " and (rp.statusPreparo <> 'CO' or rp.statusPagamento <> 'PG') ")
	List<RequisicaoProduto> findNotConcludedByUsuario(@Param("usuarioId") Long usuarioId);

	@Query(" select rp from RequisicaoProduto rp JOIN rp.requisicao r " + " where r.usuario.id = :usuarioId "
			+ " and rp.produto.id IN (:produtoIdList) and (rp.statusPreparo NOT IN ('CO','EP')) ")
	List<RequisicaoProduto> findNotEmPreparacaoByUsuarioProduto(@Param("usuarioId") Long usuarioId,
			@Param("produtoIdList") List<Long> produtoIdList);

	@Query(" select rp from RequisicaoProduto rp JOIN FETCH rp.requisicao r " + " where r.id IN (:requisicaoIdList) ")
	List<RequisicaoProduto> findByRequisicao(@Param("requisicaoIdList") List<Long> requisicaoIdList);

	@Query(" select rp from RequisicaoProduto rp join rp.requisicao r "
			+ " where r.usuario.id = :usuarioId and r.reservado = 1 order by r.dataHora desc ")
	List<RequisicaoProduto> findReservadoByUsuarioOrderDataHoraDesc(@Param("usuarioId") Long usuarioId);
}
