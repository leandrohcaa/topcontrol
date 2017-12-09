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

@Component
public class RequisicaoBusinessImpl extends AbstractBusiness<Requisicao, Long> implements RequisicaoBusiness {

	@Autowired
	private transient ProdutoManager produtoManager;
	@Autowired
	private transient RequisicaoRepository requisicaoRepository;
	@Autowired
	private transient RequisicaoProdutoRepository requisicaoProdutoRepository;

	@Override
	public BaseRepository<Requisicao, Long> getRepository() {
		return requisicaoRepository;
	}

	@Override
	public void prepare(List<GrupoProdutoProdutoDTO> produtoDTOList, Usuario usuario) {
		List<Long> produtoDistinctIdList = produtoDTOList.stream().map(d -> d.getId()).distinct()
				.collect(Collectors.toList());
		List<RequisicaoProduto> notPreparedList = requisicaoProdutoRepository
				.findNotEmPreparacaoByUsuarioProduto(usuario.getId(), produtoDistinctIdList);

		List<RequisicaoProduto> requisicaoProdutoToUpdateList = new ArrayList<>();
		List<RequisicaoProduto> requisicaoProdutoToCreateList = new ArrayList<>();
		for (GrupoProdutoProdutoDTO produtoDTO : produtoDTOList) {
			for (int i = 0; i < produtoDTO.getQuantidade(); i++) {
				Optional<RequisicaoProduto> requisicaoProdutoToUpdateOptional = notPreparedList.stream()
						.filter(rp -> rp.getProduto().equals(produtoDTO.getId())).findFirst();
				if (requisicaoProdutoToUpdateOptional.isPresent()) {
					RequisicaoProduto requisicaoProduto = notPreparedList
							.remove(notPreparedList.indexOf(requisicaoProdutoToUpdateOptional.get()));
					requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.EP);
					requisicaoProdutoToUpdateList.add(requisicaoProduto);
				} else {
					RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
					requisicaoProduto.setProduto(new Produto(produtoDTO.getId()));
					requisicaoProduto.setPreco(produtoDTO.getPreco());
					requisicaoProduto.setUrgencia(IndicadorRequisicaoProdutoUrgencia.NO); // TODO
					requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.EP);
					requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
					requisicaoProdutoToCreateList.add(requisicaoProduto);
				}
			}
		}

		if (!CollectionUtils.isEmpty(requisicaoProdutoToCreateList)) {
			Requisicao requisicao = new Requisicao();
			requisicao.setDataHora(LocalDateTime.now());
			requisicao.setUsuario(usuario);
			requisicao.setStatus(IndicadorRequisicaoStatus.NI);
			requisicao.setReservado(false);
			requisicao = requisicaoRepository.save(requisicao);

			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoToCreateList)
				requisicaoProduto.setRequisicao(requisicao);

			requisicaoProdutoRepository.save(requisicaoProdutoToCreateList);
		}

		if (!CollectionUtils.isEmpty(requisicaoProdutoToUpdateList)) {
			requisicaoProdutoRepository.save(requisicaoProdutoToUpdateList);
		}
	}

	@Override
	public void request(List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList, Usuario usuario) {
		List<GrupoProduto> grupoProdutoList = produtoManager.getGrupoProdutoList(null);

		Requisicao requisicao = new Requisicao();
		requisicao.setDataHora(LocalDateTime.now());
		requisicao.setUsuario(usuario);
		requisicao.setStatus(IndicadorRequisicaoStatus.NI);
		requisicao.setReservado(true);
		requisicao = requisicaoRepository.save(requisicao);

		int grupoProdutoSequencia = 0;
		List<RequisicaoProduto> requisicaoProdutoToAdd = new ArrayList<>();
		for (GrupoProdutoProdutoDTO grupoProdutoProdutoDTO : grupoProdutoProdutoDTOList) {
			for (int i = 0; i < grupoProdutoProdutoDTO.getQuantidade(); i++) {

				if (grupoProdutoProdutoDTO.getTipo().equals(GrupoProdutoProdutoDTO.Tipo.PRODUTO)) {
					RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
					requisicaoProduto.setRequisicao(requisicao);
					requisicaoProduto.setProduto(new Produto(grupoProdutoProdutoDTO.getId()));
					requisicaoProduto.setPreco(grupoProdutoProdutoDTO.getPreco());
					requisicaoProduto.setUrgencia(IndicadorRequisicaoProdutoUrgencia.NO); // TODO
					requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.NI);
					requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
					requisicaoProdutoToAdd.add(requisicaoProduto);
				} else {
					Optional<GrupoProduto> grupoProdutoOptional = grupoProdutoList.stream()
							.filter(gp -> gp.getId().equals(grupoProdutoProdutoDTO.getId())).findFirst();
					if (grupoProdutoOptional.isPresent()) {
						GrupoProduto grupoProduto = grupoProdutoOptional.get();
						BigDecimal precoGrupo = grupoProduto.getPreco();
						for (Produto produto : grupoProduto.getProdutoList()) {
							RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
							requisicaoProduto.setRequisicao(requisicao);
							requisicaoProduto.setGrupoProduto(grupoProduto);
							requisicaoProduto.setGrupoProdutoSequencia(grupoProdutoSequencia);
							requisicaoProduto.setProduto(produto);
							requisicaoProduto.setPreco(precoGrupo != null ? precoGrupo
									.divide(new BigDecimal(grupoProduto.getProdutoList().size()), RoundingMode.HALF_UP)
									: produto.getPreco());
							requisicaoProduto.setUrgencia(IndicadorRequisicaoProdutoUrgencia.NO); // TODO
							requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.NI);
							requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
							requisicaoProdutoToAdd.add(requisicaoProduto);
						}
					}
					grupoProdutoSequencia++;
				}
			}
		}
		requisicaoProdutoRepository.save(requisicaoProdutoToAdd);
	}

	@Override
	public List<GrupoProdutoProdutoDTO> fillPrepareResumeList(Usuario usuario) {
		List<Produto> produtoList = produtoManager.getProdutoList(null);
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		List<RequisicaoProduto> requisicaoProdutoList = requisicaoProdutoRepository
				.findNotConcludedByUsuario(usuario.getId());
		for (Long produtoId : requisicaoProdutoList.stream().map(rp -> rp.getProduto().getId()).distinct()
				.collect(Collectors.toList())) {
			Produto produto = produtoList.stream().filter(p -> p.getId().equals(produtoId)).findFirst().get();
			GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
					produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO);

			int credito = 0;
			List<RequisicaoProduto> requisicaoByProdutoList = requisicaoProdutoList.stream()
					.filter(rp -> rp.getProduto().equals(produto)).collect(Collectors.toList());
			for (RequisicaoProduto requisicaoProduto : requisicaoByProdutoList) {
				if (requisicaoProduto.getStatusPreparo().equals(IndicadorRequisicaoProdutoStatusPreparo.CO)
						&& !requisicaoProduto.getStatusPagamento().equals(IndicadorRequisicaoProdutoStatusPagamento.PG))
					credito += -1;
				else if (!requisicaoProduto.getStatusPreparo().equals(IndicadorRequisicaoProdutoStatusPreparo.CO)
						&& requisicaoProduto.getStatusPagamento().equals(IndicadorRequisicaoProdutoStatusPagamento.PG))
					credito += 1;
				else if (!requisicaoProduto.getStatusPreparo().equals(IndicadorRequisicaoProdutoStatusPreparo.CO)
						&& !requisicaoProduto.getStatusPagamento().equals(IndicadorRequisicaoProdutoStatusPagamento.PG))
					credito += -1;
			}
			dto.setCredito(credito);

			result.add(dto);
		}
		return result;
	}

	@Override
	public List<Requisicao> findByUsuarioOrderDataHoraDesc(Usuario usuario) {
		List<RequisicaoProduto> requisicaoProdutoList = requisicaoProdutoRepository
				.findReservadoByUsuarioOrderDataHoraDesc(usuario.getId());
		return requisicaoProdutoList.stream().map(rp -> rp.getRequisicao()).distinct().collect(Collectors.toList());
	}
}
