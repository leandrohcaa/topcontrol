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

	@PostConstruct
	public void init() {
		List<Produto> produtoHierarchyResult = produtoRepository.findHierarchy();
		for (Produto produto : produtoHierarchyResult)
			setVoidEntity(produto);
		produtoHierarchy = produtoHierarchyResult;
	}

	private void setVoidEntity(Produto produto) {
		produto.setProdutoPai(produto.getProdutoPai() != null ? new Produto(produto.getProdutoPai().getId()) : null);
		if (produto.getProdutoFilhoList() != null)
			for (Produto produtoFilho : produto.getProdutoFilhoList())
				setVoidEntity(produtoFilho);
	}

	@Override
	public BaseRepository<Produto, Long> getRepository() {
		return produtoRepository;
	}

	private List<Produto> produtoHierarchy;

	@Override
	public List<Produto> getProdutoHierarchy(Long usuarioNegocioId) {
		return filterByUsuarioNegocio(new ArrayList<>(produtoHierarchy), usuarioNegocioId);
	}

	@Override
	public List<Produto> getProdutoHierarchyList(boolean withProdutoFilhoList, Long usuarioNegocioId) {
		List<Produto> produtoListReturn = new ArrayList<>();
		fillList(produtoListReturn, new ArrayList<>(produtoHierarchy), withProdutoFilhoList);
		return filterByUsuarioNegocio(new ArrayList<>(produtoListReturn), usuarioNegocioId);
	}

	private List<Produto> filterByUsuarioNegocio(List<Produto> produtoListResult, Long usuarioNegocioId) {
		if (usuarioNegocioId != null) {
			produtoListResult = produtoListResult.stream()
					.filter(p -> !CollectionUtils.isEmpty(p.getUsuarioNegocioList()) && p.getUsuarioNegocioList()
							.stream().map(un -> un.getId()).collect(Collectors.toList()).contains(usuarioNegocioId))
					.collect(Collectors.toList());

			for (Produto produto : produtoListResult) {
				if (produto.getProdutoFilhoList() != null) {
					produto.setProdutoFilhoList(
							filterByUsuarioNegocio(produto.getProdutoFilhoList(), usuarioNegocioId));
				}
			}
		}
		return produtoListResult;
	}

	private void fillList(List<Produto> produtoListReturn, List<Produto> produtoListIter,
			boolean withProdutoFilhoList) {
		for (Produto produto : produtoListIter) {
			if (produto.getProdutoFilhoList() != null)
				fillList(produtoListReturn, produto.getProdutoFilhoList(), withProdutoFilhoList);
		}

		produtoListIter.stream().map(p -> {
			p = (Produto) p.clone();
			if (!withProdutoFilhoList)
				p.setProdutoFilhoList(null);
			return p;
		}).forEach(p -> produtoListReturn.add(p));
	}
}
