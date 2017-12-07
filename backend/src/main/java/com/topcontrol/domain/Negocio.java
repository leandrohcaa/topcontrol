package com.topcontrol.domain;

import javax.persistence.*;
import lombok.*;
import com.topcontrol.domain.base.*;

@Entity
@Data
@ToString(exclude = { })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "negocio")
public class Negocio extends BaseEntity<Long> {

	public Negocio(Long id, String nome, String theme) {
		super(id);
		this.nome = nome;
		this.theme = theme;
	}

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "nome", nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(name = "theme", nullable = false)
	private String theme;
		
	public Negocio() {}
	
	public Negocio(Long id) {
        this.id = id;
    }
}
