package com.topcontrol.domain.indicador;
 
public enum IndicadorRequisicaoProdutoUrgencia {
	NO("Normal"), AL("Alta");
	
	private String descricao;

    IndicadorRequisicaoProdutoUrgencia(String descricao) {
        this.descricao = descricao;
    }
	
	public String descricao() {
        return descricao;
    }
} 