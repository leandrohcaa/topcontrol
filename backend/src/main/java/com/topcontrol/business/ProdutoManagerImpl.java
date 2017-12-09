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
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.util.CollectionUtils;

@Component
public class ProdutoManagerImpl extends AbstractBusiness<Produto, Long> implements ProdutoManager {

	@Autowired
	private transient ProdutoRepository produtoRepository;
	@Autowired
	private transient GrupoProdutoRepository grupoProdutoRepository;

	private List<Produto> produtoList;
	private List<GrupoProduto> grupoProdutoList;

	@PostConstruct
	public void init() {
		produtoList = getProdutoList(null);
		grupoProdutoList = getGrupoProdutoList(null);
	}

	@Override
	public BaseRepository<Produto, Long> getRepository() {
		return produtoRepository;
	}

	@Override
	public void clearCache() {
		produtoList = null;
		grupoProdutoList = null;
	}

	@Override
	public List<Produto> getProdutoList(Long usuarioNegocioId) {
		if (produtoList == null) {
			synchronized (ProdutoManagerImpl.class) {
				if (produtoList == null) {
					produtoList = produtoRepository.findForCache();

					List<CaracteristicaProduto> caracteristcaList = produtoRepository
							.findCaracteristicaProdutoForCache();
					for (Produto produto : produtoList) {
						produto.setCaracteristicaProdutoList(caracteristcaList.stream()
								.filter(c -> c.getProduto().equals(produto)).distinct().collect(Collectors.toList()));
					}
				}
			}
		}

		return filterProdutoByUsuarioNegocio(new ArrayList<>(produtoList), usuarioNegocioId);
	}

	@Override
	public List<GrupoProduto> getGrupoProdutoList(Long usuarioNegocioId) {
		if (grupoProdutoList == null) {
			synchronized (ProdutoManagerImpl.class) {
				if (grupoProdutoList == null) {
					grupoProdutoList = grupoProdutoRepository.findForCache();

					List<Produto> produtoList = grupoProdutoRepository.findProdutoForCache();
					for (GrupoProduto grupoProduto : grupoProdutoList) {
						grupoProduto.setProdutoList(produtoList.stream()
								.filter(p -> p.getGrupoProdutoList().stream()
										.filter(gp -> gp.getId().equals(grupoProduto.getId())).count() > 0)
								.distinct().collect(Collectors.toList()));
					}
				}
			}
		}
		return filterGrupoProdutoByUsuarioNegocio(new ArrayList<>(grupoProdutoList), usuarioNegocioId);
	}

	private List<Produto> filterProdutoByUsuarioNegocio(List<Produto> produtoListResult, Long usuarioNegocioId) {
		if (usuarioNegocioId != null) {
			produtoListResult = produtoListResult.stream()
					.filter(p -> !CollectionUtils.isEmpty(p.getUsuarioNegocioList()) && p.getUsuarioNegocioList()
							.stream().map(un -> un.getId()).collect(Collectors.toList()).contains(usuarioNegocioId))
					.collect(Collectors.toList());
		}
		return produtoListResult;
	}

	private List<GrupoProduto> filterGrupoProdutoByUsuarioNegocio(List<GrupoProduto> grupoProdutoListResult,
			Long usuarioNegocioId) {
		if (usuarioNegocioId != null) {
			grupoProdutoListResult = grupoProdutoListResult.stream()
					.filter(p -> !CollectionUtils.isEmpty(p.getUsuarioNegocioList()) && p.getUsuarioNegocioList()
							.stream().map(un -> un.getId()).collect(Collectors.toList()).contains(usuarioNegocioId))
					.collect(Collectors.toList());
		}
		return grupoProdutoListResult;
	}

}
