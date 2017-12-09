package com.topcontrol.domain;

import com.topcontrol.domain.base.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import org.springframework.util.CollectionUtils;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "produto")
public class Produto extends BaseEntity<Long> {

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
	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
	private List<CaracteristicaProduto> caracteristicaProdutoList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "grupo_produto_produto", joinColumns = { @JoinColumn(name = "produto") }, inverseJoinColumns = {
			@JoinColumn(name = "grupo_produto") })
	private List<GrupoProduto> grupoProdutoList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "produto_usuario_negocio", joinColumns = { @JoinColumn(name = "produto") }, inverseJoinColumns = {
			@JoinColumn(name = "usuario_negocio") })
	private List<UsuarioNegocio> usuarioNegocioList;

	public Produto() {
	}

	public Produto(Long id) {
		this.id = id;
	}

	public Produto(Long id, String nome, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}
}
