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


export class RequisicaoDTO {
    id: number;
    datahora: Date;
    usuario: Usuario;
    grupoProdutoProdutoDTOResume: string;
    grupoProdutoProdutoDTOList: Array<GrupoProdutoProdutoDTO>;
}
export class GrupoCaracteristicaProdutoDTO {
    id: number;
    nome: string;
    pergunta: string;
    tipo: string;
    caracteristicaProdutoDTOList: Array<CaracteristicaProdutoDTO>;
}
export class CaracteristicaProdutoDTO {
    id: number;
    nome: string;
    descricao: string;
}
export class GrupoProdutoProdutoDTO {
    id: number;
    nome: string;
    descricao: string;
    preco: number;
    tipo: string;
    quantidade: number;
    emPreparacao: number;
    aPagar: number;
    aConsumir: number;
    datahora: Date;
    caracteristicaProdutoDTOResume: string;
    requisicaoProdutoId: number;
    estadoExibicao: string = 'visible';
    imagem: string;
    usuarioRequisicao: string;
    pendenteFillCaracteristicas: boolean;
    statusPreparo: string;
    statusPagamento: string;
    urgencia: CaracteristicaProdutoDTO;
    grupoCaracteristicaProdutoDTOList: Array<GrupoCaracteristicaProdutoDTO>;
    caracteristicaProdutoDTOList: Array<CaracteristicaProdutoDTO>;
}