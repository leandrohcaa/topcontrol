package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.domain.indicador.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import com.topcontrol.business.base.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class RequisicaoProdutoBusinessImpl extends AbstractBusiness<RequisicaoProduto, Long>
		implements RequisicaoProdutoBusiness {

	@Autowired
	private transient RequisicaoProdutoRepository requisicaoProdutoRepository;
	@Autowired
	private transient CaracteristicaProdutoBusiness caracteristicaProdutoBusiness;

	@Override
	public BaseRepository<RequisicaoProduto, Long> getRepository() {
		return requisicaoProdutoRepository;
	}

	@Override
	public List<RequisicaoProduto> beforeSave(List<RequisicaoProduto> dList) {
		for (RequisicaoProduto requisicaoProduto : dList) {
			requisicaoProduto.setDataHoraUltimaModificacao(LocalDateTime.now());
			if (requisicaoProduto.getAtivo() == null)
				requisicaoProduto.setAtivo(true);
		}
		return dList;
	}

	@Override
	public List<RequisicaoProduto> fetchCaracteristicaProduto(List<RequisicaoProduto> requisicaoProdutoList) {
		if (!CollectionUtils.isEmpty(requisicaoProdutoList)) {
			List<CaracteristicaProduto> caracteristicaProdutoList = caracteristicaProdutoBusiness
					.findByRequisicao(requisicaoProdutoList.stream().map(r -> r.getId()).collect(Collectors.toList()));
			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
				requisicaoProduto.setCaracteristicaProdutoList(new ArrayList<>());
				for (CaracteristicaProduto caracteristicaProduto : caracteristicaProdutoList) {
					if (caracteristicaProduto.getRequisicaoProdutoList().contains(requisicaoProduto)
							&& !requisicaoProduto.getCaracteristicaProdutoList().contains(caracteristicaProduto)) {
						requisicaoProduto.getCaracteristicaProdutoList().add(caracteristicaProduto);
					}
				}
			}
		}
		return requisicaoProdutoList;
	}

	@Override
	public List<RequisicaoProduto> findNotConcludedByUsuario(Long usuarioId) {
		return requisicaoProdutoRepository.findNotConcludedByUsuario(usuarioId);
	}

	@Override
	public List<RequisicaoProduto> findConcludedByUsuarioNotProduto(Long usuarioId, List<Long> produtoIdList) {
		return requisicaoProdutoRepository.findConcludedByUsuarioNotProduto(usuarioId, produtoIdList);
	}

	@Override
	public List<RequisicaoProduto> findNotEmPreparacaoByUsuarioProduto(Long usuarioId, List<Long> produtoIdList) {
		return requisicaoProdutoRepository.findNotEmPreparacaoByUsuarioProduto(usuarioId, produtoIdList);
	}

	@Override
	public List<RequisicaoProduto> findByRequisicao(List<Long> requisicaoIdList) {
		return requisicaoProdutoRepository.findByRequisicao(requisicaoIdList);
	}

	@Override
	public List<RequisicaoProduto> findReservadoByUsuarioOrderDataHoraDesc(Long usuarioId) {
		return requisicaoProdutoRepository.findReservadoByUsuarioOrderDataHoraDesc(usuarioId);
	}

	@Override
	public List<RequisicaoProduto> findByStatusPreparacaoUsuarioNegocio(
			List<IndicadorRequisicaoProdutoStatusPreparo> statusPreparacaoList, Long usuarioNegocioId) {
		return requisicaoProdutoRepository.findByStatusPreparacaoUsuarioNegocio(statusPreparacaoList, usuarioNegocioId);
	}

	@Override
	public List<RequisicaoProduto> findByStatusPagamentoUsuarioNegocio(
			List<IndicadorRequisicaoProdutoStatusPagamento> statusPagamentoList, Long usuarioNegocioId) {
		return requisicaoProdutoRepository.findByStatusPagamentoUsuarioNegocio(statusPagamentoList, usuarioNegocioId);
	}

	@Override
	public List<RequisicaoProduto> findByRequisicaoGrupoProdutoAndSequencia(Long requisicaoId, Long grupoProdutoId,
			Integer grupoProdutoSequencia) {
		return requisicaoProdutoRepository.findByRequisicaoGrupoProdutoAndSequencia(requisicaoId, grupoProdutoId,
				grupoProdutoSequencia);
	}

	@Override
	public LocalDateTime findMaxUltimaModificacaoByUsuarioNegocio(Long usuarioNegocioId) {
		return requisicaoProdutoRepository.findMaxUltimaModificacaoByUsuarioNegocio(usuarioNegocioId);
	}

	@Override
	public LocalDateTime findMaxUltimaModificacaoByUsuarioRequisicao(Long usuarioId) {
		return requisicaoProdutoRepository.findMaxUltimaModificacaoByUsuarioRequisicao(usuarioId);
	}
}
