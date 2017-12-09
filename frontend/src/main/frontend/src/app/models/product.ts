import { Usuario } from './user';

export class Produto {
    id: number;
    nome: string;
    descricao: string;
    preco: number;
    caracteristicaProdutoList: Array<CaracteristicaProduto>;
}
export class GrupoProduto {
    id: number;
    nome: string;
    descricao: string;
    preco: number;
    produtoList: Array<Produto>;
}
export class CaracteristicaProduto {
    id: number;
    produto: Produto;
    nome: string;
    descricao: string;
}
export class Requisicao {
    id: number;
    dataHora: Date;
    usuario: Usuario;
    status: string;
    requisicaoProdutoList: Array<RequisicaoProduto>;
}
export class RequisicaoProduto {
    id: number;
    requisicao: Requisicao;
    produto: Produto;
    grupoProduto: GrupoProduto;
    preco: number;
    statusPreparo: string;
    statusPagamento: string;
    usuarioPreparo: Usuario;
    usuarioPagamento: Usuario;
    dataHoraPreparo: Date;
    dataHoraPagamento: Date;
    urgencia: string;
}


export class GrupoProdutoProdutoDTO {
    id: number;
    nome: string;
    descricao: string;
    preco: number;
    tipo: string;
    quantidade: number;
    credito: number;
    caracteristicaProdutoDTOList: Array<CaracteristicaProdutoDTO>;
}
export class CaracteristicaProdutoDTO {
    id: number;
    nome: string;
    descricao: string;
}