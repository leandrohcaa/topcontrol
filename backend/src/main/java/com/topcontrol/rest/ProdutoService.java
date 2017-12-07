package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class ProdutoService {

	public static final String PREFIX_WEB_SERVICE = "/api/produto";

	@Autowired
	private transient ProdutoManager produtoManager;

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/hierarchyList", method = RequestMethod.GET)
	public List<Produto> findHierarchyList(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<Produto> result = new ArrayList<>();
		for (Produto produto : produtoManager.getProdutoHierarchyList(false, usuarioNegocioId))
			result.add(new Produto(produto.getId(), produto.getNome(), produto.getDescricao(),
					produto.getSelecionavel(), produto.getProdutoPai(), produto.getProdutoFilhoList()));
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/hierarchyListWithProdutoFilhoList", method = RequestMethod.GET)
	public List<Produto> findHierarchyListWithProdutoFilhoList(
			@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<Produto> result = new ArrayList<>();
		for (Produto produto : produtoManager.getProdutoHierarchyList(true, usuarioNegocioId))
			result.add(new Produto(produto.getId(), produto.getNome(), produto.getDescricao(),
					produto.getSelecionavel(), produto.getProdutoPai(), produto.getProdutoFilhoList()));
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/hierarchy", method = RequestMethod.GET)
	public List<Produto> findHierarchy(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<Produto> result = new ArrayList<>();
		for (Produto produto : produtoManager.getProdutoHierarchy(usuarioNegocioId))
			result.add(new Produto(produto.getId(), produto.getNome(), produto.getDescricao(),
					produto.getSelecionavel(), produto.getProdutoPai(), produto.getProdutoFilhoList()));
		return result;
	}
}