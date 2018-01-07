package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import com.topcontrol.business.base.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.stereotype.*;

@Component
public class UsuarioBusinessImpl extends AbstractBusiness<Usuario, Long> implements UsuarioBusiness {

	private final UsuarioRepository usuarioRepository;

	public UsuarioBusinessImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public BaseRepository<Usuario, Long> getRepository() {
		return usuarioRepository;
	}

	@Override
	public Usuario login(Usuario usuario) {
		if (usuario.getSenha() != null) {
			usuario.setSenha(cryptPassword(usuario.getSenha()));
			usuario = usuario.getDono() != null
					? usuarioRepository.login(usuario.getUsuario(), usuario.getSenha(), usuario.getDono().getId())
					: usuarioRepository.login(usuario.getUsuario(), usuario.getSenha());
		} else
			usuario = usuario.getDono() != null
					? usuarioRepository.login(usuario.getUsuario(), usuario.getDono().getId())
					: usuarioRepository.login(usuario.getUsuario());

		if (usuario == null)
			throw new BusinessException("Credenciais não permitidas.", null);

		return usuario;

	}

	@Override
	public Usuario loginOwner(Usuario usuario) {
		usuario.setSenha(cryptPassword(usuario.getSenha()));
		usuario = usuarioRepository.loginOwner(usuario.getUsuario(), usuario.getSenha());

		if (usuario == null)
			throw new BusinessException("Credenciais não permitidas.", null);
		else {
			usuario.getDono().setClienteList(usuarioRepository.fetchClienteListInUsuarioNegocio(
					usuario.getDono().getUsuario().getId(), usuario.getDono().getNegocio().getId()));
		}

		return usuario;

	}

	@Override
	public List<Usuario> beforeSave(List<Usuario> usuarioList) {
		for (Usuario usuario : usuarioList) {
			usuario.setSenha(cryptPassword(usuario.getSenha()));
		}
		return usuarioList;
	}

	private String cryptPassword(String password) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passBytes = password.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException("Erro durante crypt de password", e);
		}
		return result;
	}
}
