package com.topcontrol.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.topcontrol.domain.CaracteristicaProduto;
import com.topcontrol.domain.Usuario;

import lombok.*;

@ToString(exclude = {})
@EqualsAndHashCode(callSuper = false, of = { "id" })
public class RequisicaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private LocalDateTime datahora;

	@Getter
	@Setter
	private Usuario usuario;

	@Getter
	@Setter
	private List<GrupoProdutoProdutoDTO> grupoProdutoProdutoDTOList;

	private String grupoProdutoProdutoDTOResume;

	public String getGrupoProdutoProdutoDTOResume() {
		String result = "";
		Map<GrupoProdutoProdutoDTO, Integer> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(grupoProdutoProdutoDTOList)) {
			for (GrupoProdutoProdutoDTO grupoProdutoProdutoDTO : grupoProdutoProdutoDTOList) {
				if (!map.containsKey(grupoProdutoProdutoDTO)) {
					map.put(grupoProdutoProdutoDTO, 1);
				} else {
					map.put(grupoProdutoProdutoDTO, map.get(grupoProdutoProdutoDTO) + 1);
				}
			}

			for (GrupoProdutoProdutoDTO key : map.keySet()) {
				Integer quantity = map.get(key);
				if (!result.isEmpty())
					result += ", ";
				result += quantity + " " + key.getNome();
				if (quantity > 1)
					result += "(s)";
			}
		}
		return result;
	}

	public RequisicaoDTO() {
	}

	public RequisicaoDTO(Long id, LocalDateTime datahora, Usuario usuario) {
		super();
		this.id = id;
		this.datahora = datahora;
		this.usuario = new Usuario(usuario.getId(), usuario.getUsuario(), usuario.getNome());
	}
}
