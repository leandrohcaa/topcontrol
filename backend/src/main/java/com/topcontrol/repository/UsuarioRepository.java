package com.topcontrol.repository;

import com.topcontrol.repository.base.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcontrol.domain.*;

public interface UsuarioRepository extends BaseRepository<Usuario, Long>, UsuarioRepositoryCustom {

	@Query("select u from Usuario u join fetch u.dono un " + " join fetch un.negocio n "
			+ " where un.usuario.usuario = :user and "
			+ " u.usuario = :user and u.senha = :password ")
	Usuario loginOwner(@Param("user") String user, @Param("password") String password);

	@Query("select u from UsuarioNegocio un join un.clienteList u "
			+ " where un.usuario.id = :usuarioId and un.negocio.id = :negocioId")
	List<Usuario> fetchClienteListInUsuarioNegocio(@Param("usuarioId") Long usuarioId,
			@Param("negocioId") Long negocioId);

	@Query("select u from Usuario u where u.usuario = :user and u.senha = :password ")
	Usuario login(@Param("user") String user, @Param("password") String password);

	@Query("select u from Usuario u where u.usuario = :user and u.senha = :password and u.dono.id = :ownerId ")
	Usuario login(@Param("user") String user, @Param("password") String password, @Param("ownerId") Long ownerId);

	@Query("select u from Usuario u where u.usuario = :user ")
	Usuario login(@Param("user") String user);

	@Query("select u from Usuario u where u.usuario = :user and u.dono.id = :ownerId ")
	Usuario login(@Param("user") String user, @Param("ownerId") Long ownerId);
}
