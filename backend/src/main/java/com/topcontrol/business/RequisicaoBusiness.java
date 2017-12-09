package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;

import java.util.List;
import java.util.Map;

import com.topcontrol.business.base.*;

public interface RequisicaoBusiness extends IBusiness<Requisicao, Long> {

	void prepare(List<GrupoProdutoProdutoDTO> produtoDTOList, Usuario usuario);
	
	void request(List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList, Usuario usuario);
	
	List<GrupoProdutoProdutoDTO> fillPrepareResumeList(Usuario usuario);
	
	List<Requisicao> findByUsuarioOrderDataHoraDesc(Usuario usuario);
	
}
