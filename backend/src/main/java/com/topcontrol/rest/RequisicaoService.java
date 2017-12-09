package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

class PrepareDTO {
	public List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList;
	public Usuario usuario;

	public PrepareDTO() {
	}
}

class RequestPrepareResponseDTO {
	public List<GrupoProdutoProdutoDTO> produtoDTOList;
	public List<Requisicao> requisicaoList;

	public RequestPrepareResponseDTO() {
	}
}

@RestController
public class RequisicaoService {

	public static final String PREFIX_WEB_SERVICE = "/api/request";

	@Autowired
	private transient RequisicaoBusiness requisicaoBusiness;

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/prepare", method = RequestMethod.POST)
	public List<GrupoProdutoProdutoDTO> prepare(@RequestBody PrepareDTO dtoObject) {
		requisicaoBusiness.prepare(dtoObject.grupoProdutoProdutoDTOList, dtoObject.usuario);
		return requisicaoBusiness.fillPrepareResumeList(dtoObject.usuario);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/request", method = RequestMethod.POST)
	public RequestPrepareResponseDTO request(@RequestBody PrepareDTO dtoObject) {
		requisicaoBusiness.request(dtoObject.grupoProdutoProdutoDTOList, dtoObject.usuario);

		RequestPrepareResponseDTO result = new RequestPrepareResponseDTO();
		result.produtoDTOList = requisicaoBusiness.fillPrepareResumeList(dtoObject.usuario);
		result.requisicaoList = new ArrayList<>();
		List<Requisicao> requisicaoAuxList = requisicaoBusiness.findByUsuarioOrderDataHoraDesc(dtoObject.usuario);
		for (Requisicao requisicao : requisicaoAuxList) {
			result.requisicaoList
					.add(new Requisicao(requisicao.getId(), requisicao.getDataHora(), requisicao.getUsuario(),
							requisicao.getStatus(), requisicao.getReservado(), requisicao.getRequisicaoProdutoList()));
		}
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getprepareandrequestlist", method = RequestMethod.POST)
	public RequestPrepareResponseDTO getprepareandrequestlist(@RequestBody Usuario usuario) {
		RequestPrepareResponseDTO result = new RequestPrepareResponseDTO();
		result.produtoDTOList = requisicaoBusiness.fillPrepareResumeList(usuario);
		result.requisicaoList = new ArrayList<>();
		List<Requisicao> requisicaoAuxList = requisicaoBusiness.findByUsuarioOrderDataHoraDesc(usuario);
		for (Requisicao requisicao : requisicaoAuxList) {
			result.requisicaoList
					.add(new Requisicao(requisicao.getId(), requisicao.getDataHora(), requisicao.getUsuario(),
							requisicao.getStatus(), requisicao.getReservado(), requisicao.getRequisicaoProdutoList()));
		}
		return result;
	}
}