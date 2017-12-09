package com.topcontrol.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.*;

@Data
@ToString(exclude = {})
public class CaracteristicaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String descricao;

	public CaracteristicaProdutoDTO() {
	}

	public CaracteristicaProdutoDTO(Long id, String nome, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}
}
