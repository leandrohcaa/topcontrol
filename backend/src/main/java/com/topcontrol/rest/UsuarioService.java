package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class UsuarioService extends AbstractEntityService<Usuario, Long> {

	public static final String PREFIX_WEB_SERVICE = "/api/user";

	@Autowired
	private transient UsuarioBusiness usuarioBusiness;

	@Override
	public IBusiness<Usuario, Long> getAbstractBusiness() {
		return usuarioBusiness;
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/save", method = RequestMethod.POST)
	public void insert(@RequestBody List<Usuario> usuarioList) {
		usuarioBusiness.save(usuarioList);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/login", method = RequestMethod.POST)
	public Usuario login(@RequestBody Usuario usuario) {
		Usuario result = null;
		usuario = usuarioBusiness.login(usuario);
		result = new Usuario(usuario.getId(), usuario.getUsuario(), usuario.getSenha(), usuario.getNome(),
				usuario.getEmail(), usuario.getTelefone(), usuario.getDono());
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/loginOwner", method = RequestMethod.POST)
	public Usuario loginOwner(@RequestBody Usuario usuario) {
		Usuario result = null;
		usuario = usuarioBusiness.loginOwner(usuario);
		result = new Usuario(usuario.getId(), usuario.getUsuario(), usuario.getSenha(), usuario.getNome(),
				usuario.getEmail(), usuario.getTelefone(), usuario.getDono());

		UsuarioNegocio resultUsuarioNegocio = new UsuarioNegocio(usuario.getDono().getId(), new Usuario(result.getId()),
				new Negocio(usuario.getDono().getNegocio().getId(), usuario.getDono().getNegocio().getNome(),
						usuario.getDono().getNegocio().getTheme()),
				usuario.getDono().getUtilizaSenha(), new ArrayList<>());
		for (Usuario cliente : usuario.getDono().getClienteList()) {
			resultUsuarioNegocio.getClienteList().add(new Usuario(cliente.getId(), cliente.getUsuario()));
		}
		result.setDono(resultUsuarioNegocio);

		return result;
	}
}