package com.topcontrol.rest;

import com.topcontrol.domain.*;
import com.topcontrol.rest.base.AbstractEntityService;
import com.topcontrol.business.*;
import com.topcontrol.business.base.IBusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class UsuarioService extends AbstractEntityService<Usuario, Long> {

	public static final String PREFIX_WEB_SERVICE = "/api/usuario";

	@Autowired
	private transient UsuarioBusiness usuarioBusiness;

	@Override
	public IBusiness<Usuario, Long> getAbstractBusiness() {
		return usuarioBusiness;
	}

	@CrossOrigin
	@RequestMapping(value = PREFIX_WEB_SERVICE, method = RequestMethod.GET)
	public Usuario get(@RequestParam(value = "id") Long id) {
		return usuarioBusiness.findById(id);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/list", method = RequestMethod.GET)
	public List<Usuario> getList(@RequestParam(value = "idList") List<Long> idList) {
		return usuarioBusiness.findAllById(idList);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/save", method = RequestMethod.POST)
	public List<Usuario> insert(@RequestBody List<Usuario> usuarioList) {
		return usuarioBusiness.save(usuarioList);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = PREFIX_WEB_SERVICE + "/delete", method = RequestMethod.POST)
	public List<Usuario> delete(@RequestBody List<Usuario> usuarioList) {
		throw new UnsupportedOperationException();
	}
}