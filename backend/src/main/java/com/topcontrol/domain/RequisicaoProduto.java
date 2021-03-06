package com.topcontrol.domain;

import com.topcontrol.domain.dto.CaracteristicaProdutoDTO;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPreparo;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import org.springframework.util.CollectionUtils;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "requisicao_produto")
public class RequisicaoProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "requisicao", nullable = false)
	private Requisicao requisicao;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "produto", nullable = false)
	private Produto produto;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "grupo_produto", nullable = true)
	private GrupoProduto grupoProduto;

	@Getter
	@Setter
	@Digits(integer = 2, fraction = 0)
	@Column(name = "grupo_produto_sequencia", nullable = true, precision = 2, scale = 0)
	private Integer grupoProdutoSequencia;

	@Getter
	@Setter
	@Digits(integer = 10, fraction = 3)
	@Column(name = "preco", nullable = false, precision = 13, scale = 3)
	private BigDecimal preco;

	@Getter
	@Setter
	@Column(name = "status_preparo", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private IndicadorRequisicaoProdutoStatusPreparo statusPreparo;

	@Getter
	@Setter
	@Column(name = "status_pagamento", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private IndicadorRequisicaoProdutoStatusPagamento statusPagamento;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "usuario_preparo", nullable = true)
	private Usuario usuarioPreparo;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "usuario_pagamento", nullable = true)
	private Usuario usuarioPagamento;

	@Getter
	@Setter
	@Column(name = "datahora_requisicao_preparacao", nullable = true)
	private LocalDateTime dataHoraRequisicaoPreparacao;

	@Getter
	@Setter
	@Column(name = "datahora_preparo", nullable = true)
	private LocalDateTime dataHoraPreparo;

	@Getter
	@Setter
	@Column(name = "datahora_pagamento", nullable = true)
	private LocalDateTime dataHoraPagamento;

	@Getter
	@Setter
	@Column(name = "datahora_ultima_modificacao", nullable = false)
	private LocalDateTime dataHoraUltimaModificacao;

	@Getter
	@Setter
	@Column(name = "ativo", nullable = false)
	private Boolean ativo;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "requisicao_produto_caracteristica_produto", joinColumns = {
			@JoinColumn(name = "requisicao_produto") }, inverseJoinColumns = {
					@JoinColumn(name = "caracteristica_produto") })
	private List<CaracteristicaProduto> caracteristicaProdutoList;

	public RequisicaoProduto() {
	}

	public RequisicaoProduto(Long id) {
		this.id = id;
	}

	public void setCaracteristicaProdutoDTOList(List<CaracteristicaProdutoDTO> dtoList) {
		setCaracteristicaProdutoList(new ArrayList<>());
		if (!CollectionUtils.isEmpty(dtoList)) {
			for (CaracteristicaProdutoDTO caracteristicaProdutoDTO : dtoList) {
				getCaracteristicaProdutoList().add(new CaracteristicaProduto(caracteristicaProdutoDTO.getId(),
						caracteristicaProdutoDTO.getNome(), caracteristicaProdutoDTO.getDescricao()));
			}
		}
	}

	public RequisicaoProduto(Long id, Requisicao requisicao, Produto produto, GrupoProduto grupoProduto,
			BigDecimal preco, IndicadorRequisicaoProdutoStatusPreparo statusPreparo,
			IndicadorRequisicaoProdutoStatusPagamento statusPagamento, Usuario usuarioPreparo, Usuario usuarioPagamento,
			LocalDateTime dataHoraPreparo, LocalDateTime dataHoraPagamento) {
		super();
		this.id = id;
		this.requisicao = new Requisicao(requisicao.getId());
		this.produto = new Produto(produto.getId(), produto.getNome(), produto.getDescricao());
		if (grupoProduto != null)
			this.grupoProduto = new GrupoProduto(grupoProduto.getId());
		this.preco = preco;
		this.statusPreparo = statusPreparo;
		this.statusPagamento = statusPagamento;
		if (usuarioPreparo != null)
			this.usuarioPreparo = new Usuario(usuarioPreparo.getId(), usuarioPreparo.getNome(),
					usuarioPreparo.getUsuario());
		if (usuarioPagamento != null)
			this.usuarioPagamento = new Usuario(usuarioPagamento.getId(), usuarioPagamento.getNome(),
					usuarioPagamento.getUsuario());
		this.dataHoraPreparo = dataHoraPreparo;
		this.dataHoraPagamento = dataHoraPagamento;
	}
}
