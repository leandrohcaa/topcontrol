package com.topcontrol.domain;

import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = { })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "produto")
public class Produto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Getter
	@Setter
	@Column(name = "selecionavel", nullable = false)
	private Boolean selecionavel;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produtopai")
	private Produto produtoPai;

	@Getter
	@Setter
	@OneToMany(mappedBy = "produtoPai", fetch = FetchType.EAGER)
	private List<Produto> produtoFilhoList;

	public Produto() {
	}

	public Produto(Long id) {
		this.id = id;
	}

	public Produto(Long id, String nome, String descricao, Boolean selecionavel, Produto produtoPai, List<Produto> produtoFilhoList) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.selecionavel = selecionavel;
		this.produtoPai = produtoPai;
		this.produtoFilhoList = produtoFilhoList;
	}
}
