package com.topcontrol.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.topcontrol.domain.CaracteristicaProduto;

import lombok.*;

@Data
@ToString(exclude = {})
public class GrupoProdutoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Tipo {
		PRODUTO, GRUPO
	};

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String descricao;

	@Getter
	@Setter
	private BigDecimal preco;

	@Getter
	@Setter
	private Tipo tipo;

	@Getter
	@Setter
	private Integer quantidade;

	@Getter
	@Setter
	private Integer credito;

	@Getter
	@Setter
	private List<CaracteristicaProdutoDTO> caracteristicaProdutoDTOList;

	public GrupoProdutoProdutoDTO() {
	}

	public GrupoProdutoProdutoDTO(Long id, String nome, String descricao, BigDecimal preco, Tipo tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.tipo = tipo;
	}

	public GrupoProdutoProdutoDTO(Long id, String nome, String descricao, BigDecimal preco, Tipo tipo,
			List<CaracteristicaProduto> caracteristicaProdutoList) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.tipo = tipo;

		caracteristicaProdutoDTOList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(caracteristicaProdutoList)) {
			for (CaracteristicaProduto caracteristicaProduto : caracteristicaProdutoList) {
				caracteristicaProdutoDTOList.add(new CaracteristicaProdutoDTO(caracteristicaProduto.getId(),
						caracteristicaProduto.getNome(), caracteristicaProduto.getDescricao()));
			}
		}
	}
}
