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
import com.topcontrol.domain.GrupoCaracteristicaProduto;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPagamento;
import com.topcontrol.domain.indicador.IndicadorRequisicaoProdutoStatusPreparo;

import lombok.*;

@ToString(exclude = {})
@EqualsAndHashCode(callSuper = false, of = { "id" })
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
	private Integer aPagar;

	@Getter
	@Setter
	private Integer aConsumir;

	@Getter
	@Setter
	private Integer emPreparacao;

	@Getter
	@Setter
	private LocalDateTime datahora;

	@Getter
	@Setter
	private String usuarioRequisicao;

	@Getter
	@Setter
	private Long requisicaoProdutoId;

	@Getter
	@Setter
	private IndicadorRequisicaoProdutoStatusPreparo statusPreparo;

	@Getter
	@Setter
	private IndicadorRequisicaoProdutoStatusPagamento statusPagamento;

	@Getter
	@Setter
	private CaracteristicaProdutoDTO urgencia;

	@Getter
	@Setter
	private List<CaracteristicaProdutoDTO> caracteristicaProdutoDTOList;

	@Getter
	@Setter
	private List<GrupoCaracteristicaProdutoDTO> grupoCaracteristicaProdutoDTOList;

	@Getter
	@Setter
	private String imageBase64;

	private String caracteristicaProdutoDTOResume;

	public String getCaracteristicaProdutoDTOResume() {
		String result = "";
		Map<CaracteristicaProdutoDTO, Integer> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(caracteristicaProdutoDTOList)) {
			for (CaracteristicaProdutoDTO caracteristicaProdutoDTO : caracteristicaProdutoDTOList) {
				if (!result.isEmpty())
					result += ", ";
				result += caracteristicaProdutoDTO.getNome();
			}
		}
		return result;
	}

	public void setCaracteristicaProdutoList(List<CaracteristicaProduto> caracteristicaProdutoList,
			CaracteristicaProduto urgencia) {
		if (urgencia != null) {
			if (caracteristicaProdutoList.stream().map(c -> c.getId())
					.anyMatch(c -> c.equals(CaracteristicaProduto.ID_URGENTE))) {
				urgencia = caracteristicaProdutoList.stream()
						.filter(c -> c.getId().equals(CaracteristicaProduto.ID_URGENTE)).findFirst().get();
			}

			this.urgencia = new CaracteristicaProdutoDTO(urgencia.getId(), urgencia.getNome(), urgencia.getDescricao());
			caracteristicaProdutoList.removeIf(c -> c.getId().equals(CaracteristicaProduto.ID_NORMAL)
					|| c.getId().equals(CaracteristicaProduto.ID_URGENTE));
		}

		caracteristicaProdutoDTOList = new ArrayList<>();
		for (CaracteristicaProduto caracteristicaProduto : caracteristicaProdutoList) {
			CaracteristicaProdutoDTO dto = new CaracteristicaProdutoDTO(caracteristicaProduto.getId(),
					caracteristicaProduto.getNome(), caracteristicaProduto.getDescricao());
			caracteristicaProdutoDTOList.add(dto);
		}
	}

	public void setGrupoCaracteristicaProdutoList(List<GrupoCaracteristicaProduto> grupoCaracteristicaProdutoList) {
		grupoCaracteristicaProdutoDTOList = new ArrayList<>();
		for (GrupoCaracteristicaProduto grupoCaracteristicaProduto : grupoCaracteristicaProdutoList) {
			GrupoCaracteristicaProdutoDTO dto = new GrupoCaracteristicaProdutoDTO(grupoCaracteristicaProduto.getId(),
					grupoCaracteristicaProduto.getNome(), grupoCaracteristicaProduto.getPergunta(),
					grupoCaracteristicaProduto.getTipo());
			dto.setCaracteristicaProdutoDTOList(new ArrayList<>());
			for (CaracteristicaProduto caracteristicaProduto : grupoCaracteristicaProduto
					.getCaracteristicaProdutoList()) {
				dto.getCaracteristicaProdutoDTOList().add(new CaracteristicaProdutoDTO(caracteristicaProduto.getId(),
						caracteristicaProduto.getNome(), caracteristicaProduto.getDescricao()));
			}
			grupoCaracteristicaProdutoDTOList.add(dto);
		}
	}

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
			LocalDateTime datahora) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.tipo = tipo;
		this.datahora = datahora;
	}

	public GrupoProdutoProdutoDTO(Long id, String nome, String descricao, BigDecimal preco, Tipo tipo,
			LocalDateTime datahora, IndicadorRequisicaoProdutoStatusPreparo statusPreparo,
			IndicadorRequisicaoProdutoStatusPagamento statusPagamento) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.tipo = tipo;
		this.datahora = datahora;
		this.statusPreparo = statusPreparo;
		this.statusPagamento = statusPagamento;
	}
}
