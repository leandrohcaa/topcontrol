package com.topcontrol.business.base;

import java.io.*;
import com.topcontrol.domain.*;
import com.topcontrol.repository.base.*;
import java.util.List;

public abstract class AbstractBusiness<D extends BaseEntity<I>, I extends Serializable> {
	
    public abstract BaseRepository<D, I> getRepository();

    public boolean exists(I id){
		return getRepository().exists(id);
	}
    
    public long count(){
		return getRepository().count();
	}
    
    public void deleteAll(){
		getRepository().deleteAll();
	}
    
    public void deleteById(I id){
		getRepository().delete(id);
	}
    
    public D findById(I id){
		return getRepository().findOne(id);
	}
    
    public List<D> findAllById(List<I> idList){
		return getRepository().findAll(idList);
	}
    
    public List<D> findAll(){
		return getRepository().findAll();
	}
    
    public D save(D d){
		return getRepository().save(d);
	}
		
    public List<D> save(List<D> dList){
		return getRepository().save(dList);
	}
}
