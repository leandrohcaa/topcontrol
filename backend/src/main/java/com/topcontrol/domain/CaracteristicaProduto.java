package com.topcontrol.domain;

import java.util.List;

import javax.persistence.*;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "caracteristica_produto")
public class CaracteristicaProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "produto", nullable = false)
	private Produto produto;
	
	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "descricao", nullable = true)
	private String descricao;

	public CaracteristicaProduto() {
	}

	public CaracteristicaProduto(Long id) {
		this.id = id;
	}

	public CaracteristicaProduto(Long id, Produto produto, String nome, String descricao) {
		super();
		this.id = id;
		this.produto = produto;
		this.nome = nome;
		this.descricao = descricao;
	}
}
