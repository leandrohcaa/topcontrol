package com.topcontrol.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = { })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "usuario")
public class Usuario extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "usuario", nullable = false)
    public String usuario;
	
	@Column(name = "senha", nullable = false)
	public String senha;
	
	@Column(name = "nome", nullable = false)
	public String nome;
	
	@Column(name = "dono", nullable = false)
	public Boolean dono;
	
	public Usuario() {}
	
	public Usuario(Long id) {
        this.id = id;
    }
}
