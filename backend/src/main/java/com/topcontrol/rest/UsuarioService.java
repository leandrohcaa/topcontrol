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
				usuario.getEmail(), usuario.getTelefone(), new ArrayList<>(), usuario.getDono());
		return result;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/loginOwner", method = RequestMethod.POST)
	public Usuario loginOwner(@RequestBody Usuario usuario) {
		Usuario result = null;
		usuario = usuarioBusiness.loginOwner(usuario);
		result = new Usuario(usuario.getId(), usuario.getUsuario(), usuario.getSenha(), usuario.getNome(),
				usuario.getEmail(), usuario.getTelefone(), new ArrayList<>(), usuario.getDono());
		for (UsuarioNegocio un : usuario.getUsuarioNegocioList()) {
			UsuarioNegocio resultUsuarioNegocio = new UsuarioNegocio(un.getId(), new Usuario(result.getId()),
					new Negocio(un.getNegocio().getId(), un.getNegocio().getNome(), un.getNegocio().getTheme()),
					un.getUtilizaSenha(), new ArrayList<>(), new ArrayList<>());
			for (Usuario cliente : un.getClienteList()) {
				resultUsuarioNegocio.getClienteList().add(new Usuario(cliente.getId(), cliente.getUsuario()));
			}
			result.getUsuarioNegocioList().add(resultUsuarioNegocio);
		}
		return result;
	}
}