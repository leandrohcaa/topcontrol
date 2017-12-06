package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.business.base.*;

public interface UsuarioBusiness extends IBusiness<Usuario, Long> {

	Usuario login(Usuario usuario);

	Usuario loginOwner(Usuario usuario);
	
}
