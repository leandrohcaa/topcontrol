package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.domain.indicador.*;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;
import com.topcontrol.business.base.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.util.CollectionUtils;

@Component
public class RequisicaoBusinessImpl extends AbstractBusiness<Requisicao, Long> implements RequisicaoBusiness {

	public static final Long URGENCIA_DIFF_MINUTES = 15L;
	public static final Long RESUME_PREPARACAO_MIN = 6L;

	@Autowired
	private transient RequisicaoRepository requisicaoRepository;
	@Autowired
	private transient ProdutoManager produtoManager;
	@Autowired
	private transient RequisicaoProdutoBusiness requisicaoProdutoBusiness;

	@Override
	public BaseRepository<Requisicao, Long> getRepository() {
		return requisicaoRepository;
	}

	@Override
	public void prepare(List<GrupoProdutoProdutoDTO> produtoDTOList, Usuario usuario) {
		List<Long> produtoDistinctIdList = produtoDTOList.stream().map(d -> d.getId()).distinct()
				.collect(Collectors.toList());
		List<RequisicaoProduto> notPreparedList = requisicaoProdutoBusiness
				.findNotEmPreparacaoByUsuarioProduto(usuario.getId(), produtoDistinctIdList);

		List<RequisicaoProduto> requisicaoProdutoToUpdateList = new ArrayList<>();
		List<RequisicaoProduto> requisicaoProdutoToCreateList = new ArrayList<>();
		for (GrupoProdutoProdutoDTO produtoDTO : produtoDTOList) {
			for (int i = 0; i < produtoDTO.getQuantidade(); i++) {
				Optional<RequisicaoProduto> requisicaoProdutoToUpdateOptional = notPreparedList.stream()
						.filter(rp -> rp.getProduto().getId().equals(produtoDTO.getId())).findFirst();
				if (requisicaoProdutoToUpdateOptional.isPresent()) {
					RequisicaoProduto requisicaoProduto = notPreparedList
							.remove(notPreparedList.indexOf(requisicaoProdutoToUpdateOptional.get()));
					requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.EP);
					requisicaoProduto.setDataHoraRequisicaoPreparacao(LocalDateTime.now());
					requisicaoProduto.setCaracteristicaProdutoDTOList(produtoDTO.getCaracteristicaProdutoDTOList());
					requisicaoProdutoToUpdateList.add(requisicaoProduto);
				} else {
					RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
					requisicaoProduto.setProduto(new Produto(produtoDTO.getId()));
					requisicaoProduto.setPreco(produtoDTO.getPreco());
					requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.EP);
					requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
					requisicaoProduto.setDataHoraRequisicaoPreparacao(LocalDateTime.now());
					requisicaoProduto.setCaracteristicaProdutoDTOList(produtoDTO.getCaracteristicaProdutoDTOList());
					requisicaoProdutoToCreateList.add(requisicaoProduto);
				}
			}
		}

		if (!CollectionUtils.isEmpty(requisicaoProdutoToCreateList)) {
			Requisicao requisicao = new Requisicao();
			requisicao.setDataHora(LocalDateTime.now());
			requisicao.setUsuario(usuario);
			requisicao.setReservado(false);
			requisicao = getRepository().save(requisicao);

			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoToCreateList)
				requisicaoProduto.setRequisicao(requisicao);

			requisicaoProdutoBusiness.save(requisicaoProdutoToCreateList);
		}

		if (!CollectionUtils.isEmpty(requisicaoProdutoToUpdateList)) {
			requisicaoProdutoBusiness.save(requisicaoProdutoToUpdateList);
		}
	}

	@Override
	public void request(List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList, Usuario usuario) {
		List<GrupoProduto> grupoProdutoList = produtoManager.getGrupoProdutoList(null);

		Requisicao requisicao = new Requisicao();
		requisicao.setDataHora(LocalDateTime.now());
		requisicao.setUsuario(usuario);
		requisicao.setReservado(true);
		requisicao = getRepository().save(requisicao);

		int grupoProdutoSequencia = 0;
		List<RequisicaoProduto> requisicaoProdutoToAdd = new ArrayList<>();
		for (GrupoProdutoProdutoDTO grupoProdutoProdutoDTO : grupoProdutoProdutoDTOList) {
			for (int i = 0; i < grupoProdutoProdutoDTO.getQuantidade(); i++) {

				if (grupoProdutoProdutoDTO.getTipo().equals(GrupoProdutoProdutoDTO.Tipo.PRODUTO)) {
					RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
					requisicaoProduto.setRequisicao(requisicao);
					requisicaoProduto.setProduto(new Produto(grupoProdutoProdutoDTO.getId()));
					requisicaoProduto.setPreco(grupoProdutoProdutoDTO.getPreco());
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
							requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.NI);
							requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
							requisicaoProdutoToAdd.add(requisicaoProduto);
						}
					}
					grupoProdutoSequencia++;
				}
			}
		}
		requisicaoProdutoBusiness.save(requisicaoProdutoToAdd);
	}

	@Override
	public List<GrupoProdutoProdutoDTO> fillPrepareResumeList(Usuario usuario) {
		List<RequisicaoProduto> requisicaoProdutoList;
		List<Produto> produtoList = produtoManager.getProdutoList(null);
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();

		requisicaoProdutoList = requisicaoProdutoBusiness.findNotConcludedByUsuario(usuario.getId());
		for (Long produtoId : requisicaoProdutoList.stream().map(rp -> rp.getProduto().getId()).distinct()
				.collect(Collectors.toList())) {
			Produto produto = produtoList.stream().filter(p -> p.getId().equals(produtoId)).findFirst().get();
			GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
					produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO);

			dto.addUrgenciaAndSetGrupoCaracteristicaProdutoList(produto.getGrupoCaracteristicaProdutoList(),
					produtoManager.getGrupoCaracteristicaProduto(GrupoCaracteristicaProduto.ID_URGENCIA));

			int emPreparacao = 0, aConsumir = 0, aPagar = 0;
			List<RequisicaoProduto> requisicaoByProdutoList = requisicaoProdutoList.stream()
					.filter(rp -> rp.getProduto().equals(produto)).collect(Collectors.toList());
			for (RequisicaoProduto requisicaoProduto : requisicaoByProdutoList) {
				if (requisicaoProduto.getStatusPreparo().equals(IndicadorRequisicaoProdutoStatusPreparo.EP))
					emPreparacao++;
				if (requisicaoProduto.getStatusPreparo().equals(IndicadorRequisicaoProdutoStatusPreparo.NI))
					aConsumir++;
				if (!requisicaoProduto.getStatusPagamento().equals(IndicadorRequisicaoProdutoStatusPagamento.PG))
					aPagar++;
			}
			dto.setEmPreparacao(emPreparacao);
			dto.setAConsumir(aConsumir);
			dto.setAPagar(aPagar);

			result.add(dto);
		}

		if (result.size() < RESUME_PREPARACAO_MIN) {
			List<Long> produtoIdUsedList = result.stream().map(r -> r.getId()).distinct().collect(Collectors.toList());
			requisicaoProdutoList = requisicaoProdutoBusiness.findConcludedByUsuarioNotProduto(usuario.getId(),
					CollectionUtils.isEmpty(produtoIdUsedList) ? Arrays.asList(-1L) : produtoIdUsedList);
			if (!CollectionUtils.isEmpty(requisicaoProdutoList)) {
				for (Long produtoId : requisicaoProdutoList.stream().map(rp -> rp.getProduto().getId()).distinct()
						.collect(Collectors.toList())) {
					Produto produto = produtoList.stream().filter(p -> p.getId().equals(produtoId)).findFirst().get();
					GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
							produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO);

					dto.addUrgenciaAndSetGrupoCaracteristicaProdutoList(produto.getGrupoCaracteristicaProdutoList(),
							produtoManager.getGrupoCaracteristicaProduto(GrupoCaracteristicaProduto.ID_URGENCIA));

					result.add(dto);
				}
			}
		}

		return result;
	}

	@Override
	public List<RequisicaoDTO> fillLastRequestResumeList(Usuario usuario) {
		List<RequisicaoProduto> requisicaoProdutoList = requisicaoProdutoBusiness
				.findReservadoByUsuarioOrderDataHoraDesc(usuario.getId());
		List<Requisicao> resquisicaoList = requisicaoProdutoList.stream().map(rp -> rp.getRequisicao()).distinct()
				.collect(Collectors.toList());
		for (Requisicao requisicao : resquisicaoList) {
			requisicao.setRequisicaoProdutoList(new ArrayList<>());
			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
				if (requisicaoProduto.getRequisicao().equals(requisicao))
					requisicao.getRequisicaoProdutoList().add(requisicaoProduto);
			}
		}

		List<RequisicaoDTO> result = new ArrayList<>();
		for (Requisicao requisicao : resquisicaoList) {
			List<Integer> sequenciaUsed = new ArrayList<>();

			RequisicaoDTO requisicaoDTO = new RequisicaoDTO(requisicao.getId(), requisicao.getDataHora(),
					requisicao.getUsuario());
			requisicaoDTO.setGrupoProdutoProdutoDTOList(new ArrayList<>());

			for (RequisicaoProduto requisicaoProduto : requisicao.getRequisicaoProdutoList()) {
				if (requisicaoProduto.getGrupoProduto() == null) {
					Produto produto = produtoManager.getProduto(requisicaoProduto.getProduto().getId());
					requisicaoDTO.getGrupoProdutoProdutoDTOList()
							.add(new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(), produto.getDescricao(),
									produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO));
				} else {
					if (sequenciaUsed.contains(requisicaoProduto.getGrupoProdutoSequencia()))
						continue;

					GrupoProduto grupoProduto = produtoManager
							.getGrupoProduto(requisicaoProduto.getGrupoProduto().getId());
					requisicaoDTO.getGrupoProdutoProdutoDTOList()
							.add(new GrupoProdutoProdutoDTO(grupoProduto.getId(), grupoProduto.getNome(),
									grupoProduto.getDescricao(), grupoProduto.getPreco(),
									GrupoProdutoProdutoDTO.Tipo.GRUPO));
					sequenciaUsed.add(requisicaoProduto.getGrupoProdutoSequencia());
				}
			}
			result.add(requisicaoDTO);
		}
		return result;
	}

	@Override
	public List<GrupoProdutoProdutoDTO> fillPreparingList(Long usuarioNegocioId) {
		List<Produto> produtoList = produtoManager.getProdutoList(null);
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		List<RequisicaoProduto> requisicaoProdutoList = requisicaoProdutoBusiness.findByStatusPreparacaoUsuarioNegocio(
				Arrays.asList(IndicadorRequisicaoProdutoStatusPreparo.EP), usuarioNegocioId);
		requisicaoProdutoList = requisicaoProdutoBusiness.fetchCaracteristicaProduto(requisicaoProdutoList);
		for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
			Produto produto = produtoList.stream().filter(p -> p.getId().equals(requisicaoProduto.getProduto().getId()))
					.findFirst().get();
			GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
					produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO,
					requisicaoProduto.getDataHoraRequisicaoPreparacao(), requisicaoProduto.getStatusPreparo(),
					requisicaoProduto.getStatusPagamento());
			dto.setUsuarioRequisicao(requisicaoProduto.getRequisicao().getUsuario().getNome());
			dto.setRequisicaoProdutoId(requisicaoProduto.getId());
			dto.setCaracteristicaProdutoList(requisicaoProduto.getCaracteristicaProdutoList(),
					produtoManager.getCaracteristicaProduto(CaracteristicaProduto.ID_URGENCIA_NORMAL));
			result.add(dto);
		}

		for (GrupoProdutoProdutoDTO dto : result) {
			dto.setDatahoraForSort(
					dto.getUrgencia().getId().equals(CaracteristicaProduto.ID_URGENCIA_NORMAL) ? dto.getDatahora()
							: dto.getDatahora().plusMinutes(-URGENCIA_DIFF_MINUTES));
		}

		return result.stream().sorted((a, b) -> a.getDatahoraForSort().compareTo(b.getDatahoraForSort()))
				.collect(Collectors.toList());
	}

	@Override
	public List<GrupoProdutoProdutoDTO> fillForPaymentList(Long usuarioNegocioId) {
		List<RequisicaoProduto> requisicaoProdutoList = requisicaoProdutoBusiness.findByStatusPagamentoUsuarioNegocio(
				Arrays.asList(IndicadorRequisicaoProdutoStatusPagamento.NP), usuarioNegocioId);

		List<Integer> sequenciaUsed = new ArrayList<>();
		List<GrupoProdutoProdutoDTO> result = new ArrayList<>();
		for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
			if (requisicaoProduto.getGrupoProduto() == null) {
				Produto produto = produtoManager.getProduto(requisicaoProduto.getProduto().getId());
				GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(produto.getId(), produto.getNome(),
						produto.getDescricao(), produto.getPreco(), GrupoProdutoProdutoDTO.Tipo.PRODUTO,
						requisicaoProduto.getRequisicao().getDataHora());
				dto.setUsuarioRequisicao(requisicaoProduto.getRequisicao().getUsuario().getNome());
				dto.setRequisicaoProdutoId(requisicaoProduto.getId());
				result.add(dto);
			} else {
				if (sequenciaUsed.contains(requisicaoProduto.getGrupoProdutoSequencia()))
					continue;

				GrupoProduto grupoProduto = produtoManager.getGrupoProduto(requisicaoProduto.getGrupoProduto().getId());
				GrupoProdutoProdutoDTO dto = new GrupoProdutoProdutoDTO(grupoProduto.getId(), grupoProduto.getNome(),
						grupoProduto.getDescricao(), grupoProduto.getPreco(), GrupoProdutoProdutoDTO.Tipo.GRUPO,
						requisicaoProduto.getRequisicao().getDataHora());
				dto.setUsuarioRequisicao(requisicaoProduto.getRequisicao().getUsuario().getNome());
				dto.setRequisicaoProdutoId(requisicaoProduto.getId());
				result.add(dto);
				sequenciaUsed.add(requisicaoProduto.getGrupoProdutoSequencia());
			}
		}
		return result.stream().sorted((a, b) -> b.getDatahora().compareTo(a.getDatahora()))
				.collect(Collectors.toList());
	}

	@Override
	public Requisicao concludePreparing(Long requisicaoProdutoId) {
		RequisicaoProduto requisicaoProduto = requisicaoProdutoBusiness.findById(requisicaoProdutoId);
		requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.CO);
		requisicaoProdutoBusiness.save(requisicaoProduto);

		return requisicaoProduto.getRequisicao();
	}

	@Override
	public Requisicao cancelPreparing(Long requisicaoProdutoId) {
		RequisicaoProduto requisicaoProduto = requisicaoProdutoBusiness.findById(requisicaoProdutoId);
		requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.NI);
		requisicaoProdutoBusiness.save(requisicaoProduto);

		return requisicaoProduto.getRequisicao();
	}

	@Override
	public Requisicao cancelRequest(Long requisicaoProdutoId) {
		RequisicaoProduto requisicaoProduto = requisicaoProdutoBusiness.findById(requisicaoProdutoId);
		requisicaoProduto.setAtivo(false);
		requisicaoProdutoBusiness.save(requisicaoProduto);

		return requisicaoProduto.getRequisicao();
	}

	@Override
	public Requisicao concludePayment(Long requisicaoProdutoId) {
		List<RequisicaoProduto> requisicaoProdutoList = new ArrayList<>();

		RequisicaoProduto requisicaoProdutoCore = requisicaoProdutoBusiness.findById(requisicaoProdutoId);
		if (requisicaoProdutoCore.getGrupoProduto() == null)
			requisicaoProdutoList.add(requisicaoProdutoCore);
		else {
			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoBusiness
					.findByRequisicaoGrupoProdutoAndSequencia(requisicaoProdutoCore.getRequisicao().getId(),
							requisicaoProdutoCore.getGrupoProduto().getId(),
							requisicaoProdutoCore.getGrupoProdutoSequencia())) {
				requisicaoProdutoList.add(requisicaoProduto);
			}
		}

		for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
			requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.PG);
		}
		requisicaoProdutoBusiness.save(requisicaoProdutoList);

		return requisicaoProdutoList.get(0).getRequisicao();
	}
}
