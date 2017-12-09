package com.topcontrol.domain.indicador;
 
public enum IndicadorRequisicaoProdutoStatusPreparo {
	NI("Não Iniciado"), EP("Em Preparação"), CO("Concluído");
	
	private String descricao;

    IndicadorRequisicaoProdutoStatusPreparo(String descricao) {
        this.descricao = descricao;
    }
	
	public String descricao() {
        return descricao;
    }
} 