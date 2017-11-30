package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;
import com.topcontrol.business.base.*;
import org.springframework.stereotype.*;

@Component
public class UsuarioBusinessImpl extends AbstractBusiness<Usuario, Long> implements UsuarioBusiness {
		
    private final UsuarioRepository usuarioRepository;	
    public UsuarioBusinessImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
	
    @Override
    public BaseRepository<Usuario, Long> getRepository(){
		return usuarioRepository;
	}
}
