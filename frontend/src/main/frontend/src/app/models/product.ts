export class Produto {
    id: number;
    nome: string;
    descricao: string;
    selecionavel: boolean;
    credito: number;
    produtoList: Array<Produto>;
}
export class RequisicaoProduto {
    produto: Produto;
    quantidade: number = 1;
    preco: number = 1;
}
export class Requisicao {
    id: number;
    datahora: Date;
    requisicaoProdutoList: Array<RequisicaoProduto>;
}