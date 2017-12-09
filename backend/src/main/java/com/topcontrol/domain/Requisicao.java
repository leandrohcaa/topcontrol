package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import com.topcontrol.domain.indicador.*;

import javax.persistence.*;

import org.springframework.util.CollectionUtils;

import lombok.*;

@Entity
@ToString(exclude = {})
@Table(name = "requisicao")
public class Requisicao extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "datahora", nullable = false)
	private LocalDateTime dataHora;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "usuario", nullable = false)
	private Usuario usuario;

	@Getter
	@Setter
	@Column(name = "status", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private IndicadorRequisicaoStatus status;

	@Getter
	@Setter
	@Column(name = "reservado", nullable = false)
	private Boolean reservado;

	@Getter
	@Setter
	@OneToMany(mappedBy = "requisicao", fetch = FetchType.LAZY)
	private List<RequisicaoProduto> requisicaoProdutoList;

	public Requisicao() {
	}

	public Requisicao(Long id) {
		this.id = id;
	}

	public Requisicao(Long id, LocalDateTime dataHora, Usuario usuario, IndicadorRequisicaoStatus status,
			Boolean reservado, List<RequisicaoProduto> requisicaoProdutoList) {
		super();
		this.id = id;
		this.dataHora = dataHora;
		this.usuario = new Usuario(usuario.getId(), usuario.getNome(), usuario.getUsuario());
		this.status = status;
		this.reservado = reservado;

		this.requisicaoProdutoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(requisicaoProdutoList)) {
			for (RequisicaoProduto requisicaoProduto : requisicaoProdutoList) {
				this.requisicaoProdutoList
						.add(new RequisicaoProduto(requisicaoProduto.getId(), requisicaoProduto.getRequisicao(),
								requisicaoProduto.getProduto(), requisicaoProduto.getGrupoProduto(),
								requisicaoProduto.getPreco(), requisicaoProduto.getStatusPreparo(),
								requisicaoProduto.getStatusPagamento(), requisicaoProduto.getUsuarioPreparo(),
								requisicaoProduto.getUsuarioPagamento(), requisicaoProduto.getDataHoraPreparo(),
								requisicaoProduto.getDataHoraPagamento(), requisicaoProduto.getUrgencia()));
			}
		}
	}
}
