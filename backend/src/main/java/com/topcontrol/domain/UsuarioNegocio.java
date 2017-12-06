package com.topcontrol.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = { "usuario", "negocio" })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "usuario_negocio")
public class UsuarioNegocio extends BaseEntity<Long> {

	public UsuarioNegocio(Long id, Usuario usuario, Negocio negocio, Boolean utilizaSenha, List<Usuario> usuarioList,
			List<Usuario> clienteList) {
		super(id);
		this.usuario = usuario;
		this.negocio = negocio;
		this.utilizaSenha = utilizaSenha;
		this.usuarioList = usuarioList;
		this.clienteList = clienteList;
	}

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario", insertable = false, updatable = false)
	private Usuario usuario;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "negocio", insertable = false, updatable = false)
	private Negocio negocio;

	@Getter
	@Setter
	@Column(name = "utilizasenha", nullable = false)
	private Boolean utilizaSenha;

	@Getter
	@Setter
    @ManyToMany
    @JoinTable(name = "usuario_negocio_usuario", joinColumns = { @JoinColumn(name = "usuario_negocio") }, inverseJoinColumns = { @JoinColumn(name = "usuario") })
	private List<Usuario> usuarioList;

	@Getter
	@Setter
    @OneToMany(mappedBy = "dono", fetch = FetchType.LAZY)
    private List<Usuario> clienteList;

	public UsuarioNegocio() {
	}

	public UsuarioNegocio(Usuario usuario, Negocio negocio) {
		this.usuario = usuario;
		this.negocio = negocio;
	}
}
