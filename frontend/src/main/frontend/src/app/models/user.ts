export class Usuario {
    id: number;
    usuario: string;
    senha: string;
    nome: string;
    email: string;
    telefone: string;
    permissaoList: Array<Permissao>;
    dono: UsuarioNegocio;
}
export class Permissao {
    id: number;
    pagina: string;
    usuario: number;
	  permissaoAcaoList: Array<PermissaoAcao>;
}
export class PermissaoAcao {
    permissao: number;
    acao: string;
}
export class Negocio {
    id: number;
    nome: string;
    theme: string;
}
export class UsuarioNegocio {
    id: number;
    usuario: Usuario;
    negocio: Negocio;
    utilizaSenha: boolean;
    clienteList: Array<Usuario>;
}