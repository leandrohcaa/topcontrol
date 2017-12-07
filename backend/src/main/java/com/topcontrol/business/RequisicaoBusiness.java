package com.topcontrol.business;

import com.topcontrol.domain.*;

import java.util.List;
import java.util.Map;

import com.topcontrol.business.base.*;

public interface RequisicaoBusiness extends IBusiness<Requisicao, Long> {

	List<Produto> prepare(List<Produto> produtoList, Usuario usuario);
	
}
