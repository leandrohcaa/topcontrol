export class Usuario {
    id: number;
    usuario: string;
    senha: string;
    nome: string;
	  dono: boolean = false;
}
export class PermissaoPagina {
    pagina: string;
    permissao: string;
	  permissaoAcaoList: Array<PermissaoAcao>;
}
export class PermissaoAcao {
    acao: string;
    permissao: string;
}