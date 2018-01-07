package com.topcontrol.domain;

import java.util.List;

import javax.persistence.*;

import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.indicador.IndicadorTipoGrupoCaracteristicaProduto;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "grupo_caracteristica_produto")
public class GrupoCaracteristicaProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	public static final Long ID_URGENCIA = 11L;

	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "pergunta", nullable = true)
	private String pergunta;

	@Getter
	@Setter
	@Column(name = "tipo", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private IndicadorTipoGrupoCaracteristicaProduto tipo;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "grupo_caracteristica_caracteristica_produto", joinColumns = {
			@JoinColumn(name = "grupo_caracteristica_produto") }, inverseJoinColumns = { @JoinColumn(name = "caracteristica_produto") })
	private List<CaracteristicaProduto> caracteristicaProdutoList;

	public GrupoCaracteristicaProduto() {
	}

	public GrupoCaracteristicaProduto(Long id) {
		this.id = id;
	}

	public GrupoCaracteristicaProduto(String nome, String pergunta, IndicadorTipoGrupoCaracteristicaProduto tipo) {
		super();
		this.nome = nome;
		this.pergunta = pergunta;
		this.tipo = tipo;
	}
}
