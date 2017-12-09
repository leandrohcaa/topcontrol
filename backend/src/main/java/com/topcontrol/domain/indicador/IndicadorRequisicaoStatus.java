package com.topcontrol.domain.indicador;

public enum IndicadorRequisicaoStatus {
	NI("Não Iniciada"), CO("Concluída");
	
	private String descricao;

    IndicadorRequisicaoStatus(String descricao) {
        this.descricao = descricao;
    }
	
	public String descricao() {
        return descricao;
    }
} 