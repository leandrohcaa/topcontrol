package com.topcontrol.rest.base;

import com.topcontrol.domain.base.*;
import com.topcontrol.domain.*;
import com.topcontrol.business.base.IBusiness;

import java.io.Serializable;

public abstract class AbstractEntityService<D extends BaseEntity<I>, I extends Serializable> {
	public abstract IBusiness<D, I> getAbstractBusiness();
}