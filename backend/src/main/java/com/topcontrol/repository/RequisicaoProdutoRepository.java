package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPreparo;

public interface RequisicaoProdutoRepository
		extends BaseRepository<RequisicaoProduto, Long>, RequisicaoProdutoRepositoryCustom {

	@Query(" select rp from RequisicaoProduto rp JOIN FETCH rp.requisicao r JOIN FETCH rp.produto p "
			+ " where r.usuario.id = :usuarioId " + " and (rp.statusPreparo <> 'CO' or rp.statusPagamento <> 'PG') "
			+ " and rp.ativo = 1 ")
	List<RequisicaoProduto> findNotConcludedByUsuario(@Param("usuarioId") Long usuarioId);

	@Query(" select rp from RequisicaoProduto rp JOIN rp.requisicao r " + " where r.usuario.id = :usuarioId "
			+ " and rp.produto.id IN (:produtoIdList) and (rp.statusPreparo NOT IN ('CO','EP')) "
			+ " and rp.ativo = 1 ")
	List<RequisicaoProduto> findNotEmPreparacaoByUsuarioProduto(@Param("usuarioId") Long usuarioId,
			@Param("produtoIdList") List<Long> produtoIdList);

	@Query(" select rp from RequisicaoProduto rp JOIN FETCH rp.requisicao r " + " where r.id IN (:requisicaoIdList) "
			+ " and rp.ativo = 1 ")
	List<RequisicaoProduto> findByRequisicao(@Param("requisicaoIdList") List<Long> requisicaoIdList);

	@Query(" select rp from RequisicaoProduto rp join rp.requisicao r "
			+ " where r.usuario.id = :usuarioId and r.reservado = 1 " + " and rp.ativo = 1 "
			+ "order by r.dataHora desc ")
	List<RequisicaoProduto> findReservadoByUsuarioOrderDataHoraDesc(@Param("usuarioId") Long usuarioId);

	@Query(" select rp from RequisicaoProduto rp join rp.requisicao r join r.usuario u join u.dono un "
			+ " where un.id = :usuarioNegocioId and rp.statusPreparo IN (:statusPreparacaoList) "
			+ " and rp.ativo = 1 ")
	List<RequisicaoProduto> findByStatusPreparacaoUsuarioNegocio(
			@Param("statusPreparacaoList") List<IndicadorRequisicaoProdutoStatusPreparo> statusPreparacaoList,
			@Param("usuarioNegocioId") Long usuarioNegocioId);

	@Query(" select rp from RequisicaoProduto rp join rp.requisicao r join r.usuario u join u.dono un "
			+ " where un.id = :usuarioNegocioId and rp.statusPagamento IN (:statusPagamentoList) "
			+ " and rp.ativo = 1 ")
	List<RequisicaoProduto> findByStatusPagamentoUsuarioNegocio(
			@Param("statusPagamentoList") List<IndicadorRequisicaoProdutoStatusPagamento> statusPagamentoList,
			@Param("usuarioNegocioId") Long usuarioNegocioId);

	@Query(" select rp from RequisicaoProduto rp "
			+ " where rp.requisicao.id = :requisicaoId and rp.grupoProduto.id = :grupoProdutoId "
			+ " and rp.grupoProdutoSequencia = :grupoProdutoSequencia " + " and rp.ativo = 1 ")
	List<RequisicaoProduto> findByRequisicaoGrupoProdutoAndSequencia(@Param("requisicaoId") Long requisicaoId,
			@Param("grupoProdutoId") Long grupoProdutoId,
			@Param("grupoProdutoSequencia") Integer grupoProdutoSequencia);

	@Query(" select max(rp.dataHoraUltimaModificacao) from RequisicaoProduto rp join rp.requisicao r join r.usuario u join u.dono un where un.id = :usuarioNegocioId "
			+ " and rp.ativo = 1 ")
	LocalDateTime findMaxUltimaModificacaoByUsuarioNegocio(@Param("usuarioNegocioId") Long usuarioNegocioId);

	@Query(" select max(rp.dataHoraUltimaModificacao) from RequisicaoProduto rp join rp.requisicao r where r.usuario.id = :usuarioId "
			+ " and rp.ativo = 1 ")
	LocalDateTime findMaxUltimaModificacaoByUsuarioRequisicao(@Param("usuarioId") Long usuarioId);
}
