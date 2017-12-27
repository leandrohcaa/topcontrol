package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;
import com.topcontrol.domain.indicador.*;
import com.topcontrol.infra.BusinessException;
import com.topcontrol.repository.*;
import com.topcontrol.repository.base.*;

import com.topcontrol.business.base.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class CaracteristicaProdutoBusinessImpl extends AbstractBusiness<CaracteristicaProduto, Long> implements CaracteristicaProdutoBusiness {

	@Autowired
	private transient CaracteristicaProdutoRepository caracteristicaProdutoRepository;

	@Override
	public BaseRepository<CaracteristicaProduto, Long> getRepository() {
		return caracteristicaProdutoRepository;
	}

	@Override
	public List<CaracteristicaProduto> findByRequisicao( List<Long> requisicaoIdList){
		return caracteristicaProdutoRepository.findByRequisicao(requisicaoIdList);
	}
	
}
