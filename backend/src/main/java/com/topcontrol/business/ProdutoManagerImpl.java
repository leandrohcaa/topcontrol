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
	@Autowired
	private transient CaracteristicaProdutoRepository caracteristicaProdutoRepository;
	@Autowired
	private transient GrupoCaracteristicaProdutoRepository grupoCaracteristicaProdutoRepository;

	private List<Produto> produtoList;
	private List<GrupoProduto> grupoProdutoList;
	private List<GrupoCaracteristicaProduto> grupoCaracteristicaProdutoList;
	private List<CaracteristicaProduto> caracteristicaProdutoList;

	@PostConstruct
	public void init() {
		caracteristicaProdutoList = getCaracteristicaProdutoList();
		grupoCaracteristicaProdutoList = getGrupoCaracteristicaProdutoList();
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
	public Produto getProduto(Long id) {
		return getProdutoList(null).stream().filter(p -> p.getId().equals(id)).findFirst().get();
	}

	@Override
	public GrupoProduto getGrupoProduto(Long id) {
		return getGrupoProdutoList(null).stream().filter(p -> p.getId().equals(id)).findFirst().get();
	}

	@Override
	public GrupoCaracteristicaProduto getGrupoCaracteristicaProduto(Long id) {
		return getGrupoCaracteristicaProdutoList().stream().filter(p -> p.getId().equals(id)).findFirst().get();
	}

	@Override
	public CaracteristicaProduto getCaracteristicaProduto(Long id) {
		return getCaracteristicaProdutoList().stream().filter(p -> p.getId().equals(id)).findFirst().get();
	}

	@Override
	public List<Produto> getProdutoList(Long usuarioNegocioId) {
		if (produtoList == null) {
			synchronized (ProdutoManagerImpl.class) {
				if (produtoList == null) {
					produtoList = produtoRepository.findForCache();

					List<Produto> produtoWithGrupoCaracteristicaList = produtoRepository
							.findGrupoCaracteristicaProdutoForCache();
					for (Produto produto : produtoList) {
						produto.setGrupoCaracteristicaProdutoList(new ArrayList<>());
						for (Produto produtoWithGrupoCaracteristica : produtoWithGrupoCaracteristicaList) {
							if (produto.equals(produtoWithGrupoCaracteristica)) {
								for (GrupoCaracteristicaProduto grupoCaracteristicaProduto : produtoWithGrupoCaracteristica
										.getGrupoCaracteristicaProdutoList()) {
									GrupoCaracteristicaProduto grupoCaracteristicaProdutoForFetch = getGrupoCaracteristicaProduto(
											grupoCaracteristicaProduto.getId());
									if (!produto.getGrupoCaracteristicaProdutoList()
											.contains(grupoCaracteristicaProdutoForFetch))
										produto.getGrupoCaracteristicaProdutoList()
												.add(grupoCaracteristicaProdutoForFetch);
								}
							}
						}
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

	@Override
	public List<GrupoCaracteristicaProduto> getGrupoCaracteristicaProdutoList() {
		if (grupoCaracteristicaProdutoList == null) {
			synchronized (ProdutoManagerImpl.class) {
				if (grupoCaracteristicaProdutoList == null) {
					grupoCaracteristicaProdutoList = grupoCaracteristicaProdutoRepository.findForCache();

					List<CaracteristicaProduto> caracteristicaProdutoList = getCaracteristicaProdutoList();
					for (GrupoCaracteristicaProduto grupoCaracteristicaProduto : grupoCaracteristicaProdutoList) {
						grupoCaracteristicaProduto.setCaracteristicaProdutoList(caracteristicaProdutoList.stream()
								.filter(p -> p.getGrupoCaracteristicaProdutoList().stream()
										.filter(gp -> gp.getId().equals(grupoCaracteristicaProduto.getId()))
										.count() > 0)
								.distinct().collect(Collectors.toList()));
					}
				}
			}
		}
		return grupoCaracteristicaProdutoList;
	}

	@Override
	public List<CaracteristicaProduto> getCaracteristicaProdutoList() {
		if (caracteristicaProdutoList == null) {
			synchronized (ProdutoManagerImpl.class) {
				if (caracteristicaProdutoList == null) {
					caracteristicaProdutoList = caracteristicaProdutoRepository.findForCache();
				}
			}
		}
		return caracteristicaProdutoList;
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
