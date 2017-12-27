package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.topcontrol.business.base.*;

public interface RequisicaoBusiness extends IBusiness<Requisicao, Long> {

	void prepare(List<GrupoProdutoProdutoDTO> produtoDTOList, Usuario usuario);
	
	void request(List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList, Usuario usuario);
	
	List<GrupoProdutoProdutoDTO> fillPrepareResumeList(Usuario usuario);

	List<RequisicaoDTO> fillLastRequestResumeList(Usuario usuario);

	List<GrupoProdutoProdutoDTO> fillPreparingList(Long usuarioNegocioId);

	List<GrupoProdutoProdutoDTO> fillForPaymentList(Long usuarioNegocioId);

	Requisicao concludePreparing(Long requisicaoProdutoId);

	Requisicao concludePayment(Long requisicaoProdutoId);

	Requisicao cancelPreparing(Long requisicaoProdutoId);

	Requisicao cancelRequest(Long requisicaoProdutoId);
}
