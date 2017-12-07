package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.math.BigDecimal;
import java.time.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import com.topcontrol.domain.Indicador.*;

import lombok.*;

@Entity
@Data
@ToString(exclude = { })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "requisicao_produto")
public class RequisicaoProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requisicao", nullable = false)
	private Requisicao requisicao;
	
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto", nullable = false)
	private Produto produto;
	
	@Getter
	@Setter
    @Digits(integer = 10, fraction = 0)
    @Column(name = "preco", nullable = false, precision = 10, scale = 0)
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_preparo", nullable = true)
	private Usuario usuarioPreparo;
	
	@Getter
	@Setter
	@Column(name = "datahora_preparo", nullable = true)
	private LocalDateTime dataHoraPreparo;
	
	@Getter
	@Setter
	@Column(name = "datahora_entrega", nullable = true)
	private LocalDateTime dataHoraEntrega;
	
	@Getter
	@Setter
	@Column(name = "urgencia", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
	private IndicadorRequisicaoProdutoUrgencia urgencia;
	
	public RequisicaoProduto() {
	}

	public RequisicaoProduto(Long id) {
		this.id = id;
	}
}
