export class Usuario {
    id: number;
    usuario: string;
    senha: string;
    nome: string;
    email: string;
    telefone: string;
    permissaoList: Array<Permissao>;
    usuarioNegocioList: Array<UsuarioNegocio>;
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
    usuario: Usuario;
    negocio: Negocio;
    utilizaSenha: boolean;
    usuarioList: Array<Usuario>;
    clienteList: Array<Usuario>;
}