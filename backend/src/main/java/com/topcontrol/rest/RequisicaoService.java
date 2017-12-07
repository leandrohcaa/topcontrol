package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

class QuantidadeUsuarioDTO {
	public List<QuantidadeDTO> quantidadeDTOList;
	public Usuario usuario;

	public QuantidadeUsuarioDTO() {
	}
}

class QuantidadeDTO {
	public Produto produto;
	public int quantidade;

	public QuantidadeDTO() {
	}

	public QuantidadeDTO(Produto produto, int quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;
	}
}

@RestController
public class RequisicaoService {

	public static final String PREFIX_WEB_SERVICE = "/api/requisicao";

	@Autowired
	private transient RequisicaoBusiness requisicaoBusiness;

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/prepare", method = RequestMethod.POST)
	public List<QuantidadeDTO> prepare(@RequestBody QuantidadeUsuarioDTO dtoObject) {
		List<QuantidadeDTO> result = new ArrayList<>();

		List<Produto> produtoList= new ArrayList<>();
		for (QuantidadeDTO dto : dtoObject.quantidadeDTOList) {
			dto.produto.setQuantidade(dto.quantidade);
			produtoList.add(dto.produto);
		}

		produtoList = requisicaoBusiness.prepare(produtoList, dtoObject.usuario);

		for (Produto produto : produtoList)
			result.add(new QuantidadeDTO(produto, produto.getQuantidade()));
		return result;
	}
}