package com.topcontrol.domain.indicador;
 
public enum IndicadorTipoGrupoCaracteristicaProduto {
	SU("Seleção Única"), SM("Seleção Múltipla");
	
	private String descricao;

	IndicadorTipoGrupoCaracteristicaProduto(String descricao) {
        this.descricao = descricao;
    }
	
	public String descricao() {
        return descricao;
    }
} 