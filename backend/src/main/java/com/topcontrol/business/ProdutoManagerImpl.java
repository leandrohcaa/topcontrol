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

	@Getter
	private List<Produto> produtoHierarchy;

	public List<Produto> getProdutoHierarchyList() {
		List<Produto> produtoListReturn = new ArrayList<>();
		fillList(produtoListReturn, new ArrayList<>(produtoHierarchy));
		return produtoListReturn;
	}

	private void fillList(List<Produto> produtoListReturn, List<Produto> produtoListIter) {
		for (Produto produto : produtoListIter) {
			if (produto.getProdutoFilhoList() != null)
				fillList(produtoListReturn, produto.getProdutoFilhoList());
		}

		produtoListIter.stream().map(p -> {
			p = (Produto) p.clone();
			p.setProdutoFilhoList(null);
			return p;
		}).forEach(p -> produtoListReturn.add(p));
	}
}
