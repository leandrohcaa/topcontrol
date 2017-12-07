package com.topcontrol.domain.Indicador;
 
public enum IndicadorRequisicaoProdutoStatusPagamento {
	NP("Não Pago"), PG("Pago");
	
	private String descricao;

	IndicadorRequisicaoProdutoStatusPagamento(String descricao) {
        this.descricao = descricao;
    }
	
	public String descricao() {
        return descricao;
    }
} 