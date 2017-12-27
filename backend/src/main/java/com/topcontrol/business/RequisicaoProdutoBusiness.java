package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPreparo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.topcontrol.business.base.*;

public interface RequisicaoProdutoBusiness extends IBusiness<RequisicaoProduto, Long> {

	List<RequisicaoProduto> findNotConcludedByUsuario(Long usuarioId);

	List<RequisicaoProduto> findNotEmPreparacaoByUsuarioProduto(Long usuarioId, List<Long> produtoIdList);

	List<RequisicaoProduto> findByRequisicao(List<Long> requisicaoIdList);

	List<RequisicaoProduto> findReservadoByUsuarioOrderDataHoraDesc(Long usuarioId);

	List<RequisicaoProduto> findByStatusPreparacaoUsuarioNegocio(List<IndicadorRequisicaoProdutoStatusPreparo> statusPreparacaoList, Long usuarioNegocioId);

	List<RequisicaoProduto> findByStatusPagamentoUsuarioNegocio(List<IndicadorRequisicaoProdutoStatusPagamento> statusPagamentoList, Long usuarioNegocioId);

	List<RequisicaoProduto> findByRequisicaoGrupoProdutoAndSequencia(Long requisicaoId, Long grupoProdutoId, Integer grupoProdutoSequencia);
			
	LocalDateTime findMaxUltimaModificacaoByUsuarioNegocio(Long usuarioNegocioId);
	
	LocalDateTime findMaxUltimaModificacaoByUsuarioRequisicao(Long usuarioId);

	List<RequisicaoProduto> fetchCaracteristicaProduto(List<RequisicaoProduto> requisicaoProdutoList);
}
