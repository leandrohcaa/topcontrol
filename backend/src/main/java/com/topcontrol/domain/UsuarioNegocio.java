package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = { "usuario", "negocio" })
@Table(name = "usuario_negocio")
public class UsuarioNegocio extends BaseEntity<Long> {

	public UsuarioNegocio(Long id, Usuario usuario, Negocio negocio, Boolean utilizaSenha, List<Usuario> clienteList) {
		super(id);
		this.usuario = usuario;
		this.negocio = negocio;
		this.utilizaSenha = utilizaSenha;
		this.clienteList = clienteList;
	}

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "usuario", insertable = false, updatable = false)
	private Usuario usuario;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "negocio", insertable = false, updatable = false)
	private Negocio negocio;

	@Getter
	@Setter
	@Column(name = "utilizasenha", nullable = false)
	private Boolean utilizaSenha;

	@Getter
	@Setter
	@OneToMany(mappedBy = "dono", fetch = FetchType.LAZY)
	private List<Usuario> clienteList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "produto_usuario_negocio", joinColumns = {
			@JoinColumn(name = "usuario_negocio") }, inverseJoinColumns = { @JoinColumn(name = "produto") })
	private List<Produto> produtoList;

	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "grupo_produto_usuario_negocio", joinColumns = {
			@JoinColumn(name = "usuario_negocio") }, inverseJoinColumns = { @JoinColumn(name = "grupo_produto") })
	private List<GrupoProduto> grupoProdutoList;

	public UsuarioNegocio() {
	}

	public UsuarioNegocio(Long id) {
		this.id = id;
	}

	public UsuarioNegocio(Usuario usuario, Negocio negocio) {
		this.usuario = usuario;
		this.negocio = negocio;
	}
}
