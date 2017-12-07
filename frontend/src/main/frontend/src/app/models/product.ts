import { Usuario } from './user';

export class Produto {
    id: number;
    nome: string;
    descricao: string;
    selecionavel: boolean;
    produtoPai: Produto;
    produtoFilhoList: Array<Produto>;
}
export class ProdutoQuantidadeDTO {
    produto: Produto;
    quantidade: number;
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
    preco: number = 1;
    statusPreparo: string;
    statusPagamento: string;
    usuarioPreparo: Usuario;
    dataHoraPreparo: Date;
    dataHoraEntrega: Date;
    urgencia: string;
}