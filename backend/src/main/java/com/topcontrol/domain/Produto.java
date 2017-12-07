package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.springframework.util.CollectionUtils;

import lombok.*;

@Entity
@Data
@ToString(exclude = {})
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

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "produto_usuario_negocio", joinColumns = { @JoinColumn(name = "produto") }, inverseJoinColumns = {
			@JoinColumn(name = "usuario_negocio") })
	private List<UsuarioNegocio> usuarioNegocioList;

	@Getter
	@Setter
	@Transient
	private Integer quantidade;

	public Produto() {
	}

	public Produto(Long id) {
		this.id = id;
	}

	public Produto(Long id, String nome, String descricao, Boolean selecionavel, Produto produtoPai,
			List<Produto> produtoFilhoList) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.selecionavel = selecionavel;
		if (produtoPai != null)
			this.produtoPai = new Produto(produtoPai.getId());

		this.produtoFilhoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(produtoFilhoList)) {
			for (Produto produtoFilho : produtoFilhoList) {
				this.produtoFilhoList.add(new Produto(produtoFilho.getId(), produtoFilho.getNome(),
						produtoFilho.getDescricao(), produtoFilho.getSelecionavel(), produtoFilho.getProdutoPai(),
						produtoFilho.getProdutoFilhoList()));
			}
		}
	}
}
