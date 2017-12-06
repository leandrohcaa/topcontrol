package com.topcontrol.business.base;

import java.io.*;
import com.topcontrol.domain.*;
import com.topcontrol.repository.base.*;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractBusiness<D extends BaseEntity<I>, I extends Serializable> {

	public abstract BaseRepository<D, I> getRepository();

	public boolean exists(I id) {
		return getRepository().exists(id);
	}

	public long count() {
		return getRepository().count();
	}

	public void deleteAll() {
		getRepository().deleteAll();
	}

	public void deleteById(I id) {
		getRepository().delete(id);
	}

	public D findById(I id) {
		return getRepository().findOne(id);
	}

	public List<D> findAllById(List<I> idList) {
		return getRepository().findAll(idList);
	}

	public List<D> findAll() {
		return getRepository().findAll();
	}

	public D save(D d) {
		return save(Arrays.asList(d)).get(0);
	}

	public List<D> save(List<D> dList) {
		dList = beforeSave(dList);
		dList = getRepository().save(dList);
		dList = afterSave(dList);
		return dList;
	}

	public List<D> beforeSave(List<D> dList) {
		return dList;
	}

	public List<D> afterSave(List<D> dList) {
		return dList;
	}
}
