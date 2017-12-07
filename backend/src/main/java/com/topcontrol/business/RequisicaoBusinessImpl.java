package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.Indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.Indicador.IndicadorRequisicaoProdutoStatusPreparo;
import com.topcontrol.domain.Indicador.IndicadorRequisicaoProdutoUrgencia;
import com.topcontrol.domain.Indicador.IndicadorRequisicaoStatus;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import com.topcontrol.business.base.*;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Component
public class RequisicaoBusinessImpl extends AbstractBusiness<Requisicao, Long> implements RequisicaoBusiness {

	@Autowired
	private transient RequisicaoRepository requisicaoRepository;
	@Autowired
	private transient RequisicaoProdutoRepository requisicaoProdutoRepository;

	@Override
	public BaseRepository<Requisicao, Long> getRepository() {
		return requisicaoRepository;
	}

	@Override
	public List<Produto> prepare(List<Produto> produtoList, Usuario usuario) {
		List<Produto> result = new ArrayList<>();

		Requisicao requisicao = new Requisicao();
		requisicao.setDataHora(LocalDateTime.now());
		requisicao.setUsuario(usuario);
		requisicao.setStatus(IndicadorRequisicaoStatus.NI);
		requisicao = requisicaoRepository.save(requisicao);
		requisicao.setRequisicaoProdutoList(new ArrayList<>());

		List<RequisicaoProduto> requisicaoProdutoToAdd = new ArrayList<>();
		for (Produto produto : produtoList) {
			for (int i = 0; i < produto.getQuantidade(); i++) {
				RequisicaoProduto requisicaoProduto = new RequisicaoProduto();
				requisicaoProduto.setRequisicao(requisicao);
				requisicaoProduto.setProduto(produto);
				requisicaoProduto.setPreco(new BigDecimal(0));// TODO
				requisicaoProduto.setUrgencia(IndicadorRequisicaoProdutoUrgencia.NO); //TODO
				requisicaoProduto.setStatusPreparo(IndicadorRequisicaoProdutoStatusPreparo.NI);
				requisicaoProduto.setStatusPagamento(IndicadorRequisicaoProdutoStatusPagamento.NP);
				requisicaoProdutoToAdd.add(requisicaoProduto);
			}
		}
		requisicao.getRequisicaoProdutoList().addAll(requisicaoProdutoRepository.save(requisicaoProdutoToAdd));
		
		//TODO Buscar acesso rapido de preparação

		return result;
	}
}
