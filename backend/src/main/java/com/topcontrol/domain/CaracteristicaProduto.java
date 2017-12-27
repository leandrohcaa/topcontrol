package com.topcontrol.domain;

import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = { })
@Table(name = "caracteristica_produto")
public class CaracteristicaProduto extends BaseEntity<Long> {

	public CaracteristicaProduto(Long id, String nome, String descricao) {
		super(id);
		this.nome = nome;
		this.descricao = descricao;
	}

	private static final long serialVersionUID = 1L;
	public static final Long ID_NORMAL = 7L;
	public static final Long ID_URGENTE = 8L;

	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "descricao", nullable = true)
	private String descricao;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "grupo_caracteristica_caracteristica_produto", joinColumns = {
			@JoinColumn(name = "caracteristica_produto") }, inverseJoinColumns = { @JoinColumn(name = "grupo_caracteristica_produto") })
	private List<GrupoCaracteristicaProduto> grupoCaracteristicaProdutoList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "requisicao_produto_caracteristica_produto", joinColumns = {
			@JoinColumn(name = "caracteristica_produto") }, inverseJoinColumns = { @JoinColumn(name = "requisicao_produto") })
	private List<RequisicaoProduto> requisicaoProdutoList;
		
	public CaracteristicaProduto() {}
	
	public CaracteristicaProduto(Long id) {
        this.id = id;
    }
}
