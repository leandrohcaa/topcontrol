package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import lombok.Getter;

import com.topcontrol.business.base.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

public interface ProdutoManager extends IBusiness<Produto, Long> {

	List<Produto> getProdutoList(Long usuarioNegocioId);

	List<GrupoProduto> getGrupoProdutoList(Long usuarioNegocioId);

	void clearCache();

}
