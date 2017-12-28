package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

class RequestPrepareRequestDTO {
	public List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList;
	public Usuario usuario;

	public RequestPrepareRequestDTO() {
	}
}

class RequestPrepareResponseDTO {
	public List<GrupoProdutoProdutoDTO> preparacaoList;
	public List<RequisicaoDTO> requisicaoList;
	public LocalDateTime lastModification;

	public RequestPrepareResponseDTO() {
	}
}

class PreparingForPaymentResponseDTO {
	public List<GrupoProdutoProdutoDTO> list;
	public LocalDateTime lastModification;

	public PreparingForPaymentResponseDTO() {
	}
}

@RestController
public class RequisicaoService {

	public static final String PREFIX_WEB_SERVICE = "/api/request";

	@Autowired
	private transient RequisicaoBusiness requisicaoBusiness;
	@Autowired
	private transient ProdutoManager produtoManager;
	@Autowired
	private transient RequisicaoProdutoBusiness requisicaoProdutoBusiness;

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/prepare", method = RequestMethod.POST)
	public RequestPrepareResponseDTO prepare(@RequestBody RequestPrepareRequestDTO dtoObject) {
		requisicaoBusiness.prepare(dtoObject.grupoProdutoProdutoDTOList, dtoObject.usuario);

		RequestPrepareResponseDTO result = new RequestPrepareResponseDTO();
		result.preparacaoList = requisicaoBusiness.fillPrepareResumeList(dtoObject.usuario);
		result.preparacaoList.forEach(dto -> dto = produtoManager.fillImage(dto));	
		result.lastModification = requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioRequisicao(dtoObject.usuario.getId());
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/request", method = RequestMethod.POST)
	public RequestPrepareResponseDTO request(@RequestBody RequestPrepareRequestDTO dtoObject) {
		requisicaoBusiness.request(dtoObject.grupoProdutoProdutoDTOList, dtoObject.usuario);

		RequestPrepareResponseDTO result = new RequestPrepareResponseDTO();
		result.preparacaoList = requisicaoBusiness.fillPrepareResumeList(dtoObject.usuario);
		result.preparacaoList.forEach(dto -> dto = produtoManager.fillImage(dto));	
		result.requisicaoList = requisicaoBusiness.fillLastRequestResumeList(dtoObject.usuario);
		result.lastModification = requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioRequisicao(dtoObject.usuario.getId());
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getprepareandrequestresumelist", method = RequestMethod.POST)
	public RequestPrepareResponseDTO getprepareandrequestresumelist(@RequestBody Usuario usuario) {
		RequestPrepareResponseDTO result = new RequestPrepareResponseDTO();
		result.preparacaoList = requisicaoBusiness.fillPrepareResumeList(usuario);
		result.preparacaoList.forEach(dto -> dto = produtoManager.fillImage(dto));		
		result.requisicaoList = requisicaoBusiness.fillLastRequestResumeList(usuario);
		result.lastModification = requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioRequisicao(usuario.getId());
		return result;
	}
	
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getprepareresumelist", method = RequestMethod.GET)
	public List<GrupoProdutoProdutoDTO> getprepareresumelist(@RequestParam("usuarioId") Long usuarioId) {
		List<GrupoProdutoProdutoDTO> result =  requisicaoBusiness.fillPrepareResumeList(new Usuario(usuarioId));
		result.forEach(dto -> dto = produtoManager.fillImage(dto));
		return result;
	}
	
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getpreparinglist", method = RequestMethod.GET)
	public PreparingForPaymentResponseDTO getpreparinglist(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		PreparingForPaymentResponseDTO result = new PreparingForPaymentResponseDTO();
		result.list = requisicaoBusiness.fillPreparingList(usuarioNegocioId);
		result.lastModification = requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(usuarioNegocioId);
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getforpaymentlist", method = RequestMethod.GET)
	public PreparingForPaymentResponseDTO getforpaymentlist(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		PreparingForPaymentResponseDTO result = new PreparingForPaymentResponseDTO();
		result.list = requisicaoBusiness.fillForPaymentList(usuarioNegocioId);
		result.lastModification = requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(usuarioNegocioId);
		return result;
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/concludepreparing", method = RequestMethod.GET)
	public LocalDateTime concludepreparing(@RequestParam("requisicaoProdutoId") Long requisicaoProdutoId) {
		Requisicao requisicao = requisicaoBusiness.concludePreparing(requisicaoProdutoId);
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(requisicao.getUsuario().getDono().getId());
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/cancelpreparing", method = RequestMethod.GET)
	public LocalDateTime cancelpreparing(@RequestParam("requisicaoProdutoId") Long requisicaoProdutoId) {
		Requisicao requisicao = requisicaoBusiness.cancelPreparing(requisicaoProdutoId);
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(requisicao.getUsuario().getDono().getId());
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/cancelrequest", method = RequestMethod.GET)
	public LocalDateTime cancelrequest(@RequestParam("requisicaoProdutoId") Long requisicaoProdutoId) {
		Requisicao requisicao = requisicaoBusiness.cancelRequest(requisicaoProdutoId);
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(requisicao.getUsuario().getDono().getId());
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/concludepayment", method = RequestMethod.GET)
	public LocalDateTime concludepayment(@RequestParam("requisicaoProdutoId") Long requisicaoProdutoId) {
		Requisicao requisicao = requisicaoBusiness.concludePayment(requisicaoProdutoId);
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(requisicao.getUsuario().getDono().getId());
	}
	
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getlastmodificationrequisicaoprodutobyuser", method = RequestMethod.GET)
	public LocalDateTime getlastmodificationrequisicaoprodutobyuser(@RequestParam("usuarioId") Long usuarioId) {
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioRequisicao(usuarioId);
	}
	
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/getlastmodificationrequisicaoprodutobydono", method = RequestMethod.GET)
	public LocalDateTime getlastmodificationrequisicaoprodutobydono(@RequestParam("usuarioNegocioId") Long usuarioNegocioId) {
		return requisicaoProdutoBusiness.findMaxUltimaModificacaoByUsuarioNegocio(usuarioNegocioId);
	}
}