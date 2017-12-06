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
	public List<Produto> findHierarchyList() {
		return produtoManager.getProdutoHierarchyList();
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/hierarchy", method = RequestMethod.GET)
	public List<Produto> findHierarchy() {
		return produtoManager.getProdutoHierarchy();
	}
}