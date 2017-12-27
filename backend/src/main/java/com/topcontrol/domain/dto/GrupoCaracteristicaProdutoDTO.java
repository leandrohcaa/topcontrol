package com.topcontrol.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.topcontrol.domain.indicador.IndicadorTipoGrupoCaracteristicaProduto;

import lombok.*;

@ToString(exclude = {})
@EqualsAndHashCode(callSuper = false, of = { "id" })
public class GrupoCaracteristicaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String pergunta;

	@Getter
	@Setter
	private IndicadorTipoGrupoCaracteristicaProduto tipo;

	@Getter
	@Setter
	private List<CaracteristicaProdutoDTO> caracteristicaProdutoDTOList;

	public GrupoCaracteristicaProdutoDTO() {
	}

	public GrupoCaracteristicaProdutoDTO(Long id, String nome, String pergunta,
			IndicadorTipoGrupoCaracteristicaProduto tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.pergunta = pergunta;
		this.tipo = tipo;
	}

}
