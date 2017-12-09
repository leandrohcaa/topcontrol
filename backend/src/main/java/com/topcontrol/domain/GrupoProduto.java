package com.topcontrol.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "grupo_produto")
public class GrupoProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
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
    @Digits(integer = 10, fraction = 3)
    @Column(name = "preco", nullable = false, precision = 13, scale = 3)
    private BigDecimal preco;

	@Getter
	@Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "grupo_produto_p", joinColumns = { @JoinColumn(name = "grupo_produto") }, inverseJoinColumns = { @JoinColumn(name = "produto") })
	private List<Produto> produtoList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "grupo_produto_usuario_negocio", joinColumns = { @JoinColumn(name = "grupo_produto") }, inverseJoinColumns = {
			@JoinColumn(name = "usuario_negocio") })
	private List<UsuarioNegocio> usuarioNegocioList;

	public GrupoProduto() {
	}

	public GrupoProduto(Long id) {
		this.id = id;
	}

	public GrupoProduto(Long id, String nome, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}
}
