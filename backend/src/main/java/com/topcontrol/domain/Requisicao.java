package com.topcontrol.domain;

import com.topcontrol.domain.base.*;
import java.time.*;
import java.util.List;

import com.topcontrol.domain.Indicador.*;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = { })
@EqualsAndHashCode(callSuper = false, of = "id")
@Table(name = "requisicao")
public class Requisicao extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "datahora", nullable = false)
	private LocalDateTime dataHora;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario", nullable = false)
	private Usuario usuario;

	@Getter
	@Setter
	@Column(name = "status", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
	private IndicadorRequisicaoStatus status;

	@Getter
	@Setter
	@OneToMany(mappedBy = "requisicao", fetch = FetchType.LAZY)
	private List<RequisicaoProduto> requisicaoProdutoList;

	public Requisicao() {
	}

	public Requisicao(Long id) {
		this.id = id;
	}
}
