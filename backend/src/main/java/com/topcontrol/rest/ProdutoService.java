package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.GrupoProdutoProdutoDTO;
import com.topcontrol.domain.dto.RequisicaoDTO;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

class SaveImageDTO {
	public GrupoProdutoProdutoDTO product;
	public String image;

	public SaveImageDTO() {
	}
}

@RestController
public class ProdutoService {

	public static final String PREFIX_WEB_SERVICE = "/api/product";

	@Autowired
	private transient ProdutoManager produtoManager;

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/productlist", method = RequestMethod.GET)
	public List<GrupoProdutoProdutoDTO> productlist(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		for (Produto produto : produtoManager.getProdutoList(usuarioNegocioId)) {
			GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
					produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO);

			GrupoCaracteristicaProduto grupoUrgencia = produtoManager
					.getGrupoCaracteristicaProduto(GrupoCaracteristicaProduto.ID_URGENCIA);
			produto.getGrupoCaracteristicaProdutoList().add(0, grupoUrgencia);
			dto.setGrupoCaracteristicaProdutoList(produto.getGrupoCaracteristicaProdutoList());

			result.add(dto);
		}
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/grouplist", method = RequestMethod.GET)
	public List<GrupoProdutoProdutoDTO> grouplist(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		for (GrupoProduto grupoProduto : produtoManager.getGrupoProdutoList(usuarioNegocioId))
			result.add(new GrupoProdutoProdutoDTO(grupoProduto.getId(), grupoProduto.getNome(),
					grupoProduto.getDescricao(), grupoProduto.getPreco(), GrupoProdutoProdutoDTO.Tipo.GRUPO));
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/productandgrouplist", method = RequestMethod.GET)
	public List<GrupoProdutoProdutoDTO> productandgrouplist(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		for (Produto produto : produtoManager.getProdutoList(usuarioNegocioId)) {
			GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
					produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO);
			dto.setGrupoCaracteristicaProdutoList(produto.getGrupoCaracteristicaProdutoList());
			result.add(dto);
		}
		for (GrupoProduto grupoProduto : produtoManager.getGrupoProdutoList(usuarioNegocioId))
			result.add(new GrupoProdutoProdutoDTO(grupoProduto.getId(), grupoProduto.getNome(),
					grupoProduto.getDescricao(), grupoProduto.getPreco(), GrupoProdutoProdutoDTO.Tipo.GRUPO));
		return result;
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/clearCache", method = RequestMethod.GET)
	public void clearCache() {
		produtoManager.clearCache();
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/saveImage", method = RequestMethod.POST)
	public void saveImage(@RequestBody SaveImageDTO request) {
		produtoManager.saveImage(request.product, request.image);
	}
}