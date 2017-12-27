package com.topcontrol.business;

import com.topcontrol.domain.*;
import com.topcontrol.domain.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.topcontrol.business.base.*;

public interface CaracteristicaProdutoBusiness extends IBusiness<CaracteristicaProduto, Long> {

	List<CaracteristicaProduto> findByRequisicao(List<Long> requisicaoIdList);

}
