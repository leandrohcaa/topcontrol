package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = { "usuarioNegocioList", "dono" })
@Table(name = "usuario")
public class Usuario extends BaseEntity<Long> {

	private static final long serialVersionUID = 6866141782742841011L;

	@Getter
	@Setter
	@Column(name = "usuario", nullable = false)
	private String usuario;

	@Getter
	@Setter
	@Column(name = "senha", nullable = false)
	private String senha;

	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "email", nullable = false)
	private String email;

	@Getter
	@Setter
	@Column(name = "telefone", nullable = true)
	private String telefone;

	@Getter
	@Setter
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioNegocio> usuarioNegocioList;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "dono")
	private UsuarioNegocio dono;

	public Usuario(Long id, String usuario, String senha, String nome, String email, String telefone,
			List<UsuarioNegocio> usuarioNegocioList, UsuarioNegocio dono) {
		super(id);
		this.usuario = usuario;
		this.senha = senha;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.usuarioNegocioList = usuarioNegocioList;
		if (dono != null)
			this.dono = new UsuarioNegocio(dono.getId());
	}

	public Usuario() {
	}

	public Usuario(Long id) {
		this.id = id;
	}

	public Usuario(Long id, String usuario, String nome) {
		this.id = id;
		this.usuario = usuario;
		this.nome = nome;
	}

	public Usuario(Long id, String usuario) {
		this.id = id;
		this.usuario = usuario;
	}
}
