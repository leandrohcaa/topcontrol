package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.GrupoProdutoProdutoDTO;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import com.topcontrol.business.base.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

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

	public static final String IMAGE_TYPE = "jpeg";
	public static final String PATH_IMAGE_PRODUCT = "./image/product/";

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
		return (Produto) getProdutoList(null).stream().filter(p -> p.getId().equals(id)).findFirst().get().clone();
	}

	@Override
	public GrupoProduto getGrupoProduto(Long id) {
		return (GrupoProduto) getGrupoProdutoList(null).stream().filter(p -> p.getId().equals(id)).findFirst().get()
				.clone();
	}

	@Override
	public GrupoCaracteristicaProduto getGrupoCaracteristicaProduto(Long id) {
		return (GrupoCaracteristicaProduto) getGrupoCaracteristicaProdutoList().stream()
				.filter(p -> p.getId().equals(id)).findFirst().get().clone();
	}

	@Override
	public CaracteristicaProduto getCaracteristicaProduto(Long id) {
		return (CaracteristicaProduto) getCaracteristicaProdutoList().stream().filter(p -> p.getId().equals(id))
				.findFirst().get().clone();
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
									if (!produto.getGrupoCaracteristicaProdutoList().stream().map(p -> p.getId())
											.collect(Collectors.toList())
											.contains(grupoCaracteristicaProdutoForFetch.getId()))
										produto.getGrupoCaracteristicaProdutoList()
												.add(grupoCaracteristicaProdutoForFetch);
								}
							}
						}
					}
				}
			}
		}

		return new ArrayList<>(filterProdutoByUsuarioNegocio(new ArrayList<>(produtoList), usuarioNegocioId));
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
		return new ArrayList<>(filterGrupoProdutoByUsuarioNegocio(new ArrayList<>(grupoProdutoList), usuarioNegocioId));
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
		return new ArrayList<>(grupoCaracteristicaProdutoList);
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
		return new ArrayList<>(caracteristicaProdutoList);
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

	@Override
	public void saveImage(GrupoProdutoProdutoDTO dto, String image) {
		String base64Image = image.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img;
		try {
			Path path = Paths.get(PATH_IMAGE_PRODUCT);
			if (!Files.exists(path))
				Files.createDirectories(path);
			
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			ImageIO.write(img, IMAGE_TYPE, new File(PATH_IMAGE_PRODUCT + dto.getId() + "." + IMAGE_TYPE));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public GrupoProdutoProdutoDTO fillImage(GrupoProdutoProdutoDTO dto) {
		Path path = Paths.get(PATH_IMAGE_PRODUCT + dto.getId() + "." + IMAGE_TYPE);
		if (Files.exists(path)) {
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(path);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			dto.setImageBase64(Base64.getEncoder().encodeToString(bytes));
		}
		return dto;
	}
}
