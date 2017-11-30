package com.topcontrol.business.base;

import java.io.*;
import com.topcontrol.domain.*;
import java.util.List;

public interface IBusiness<D extends BaseEntity<I>, I extends Serializable> {

    public boolean exists(I id);
    
    public long count();
    
    public void deleteAll();
    
    public void deleteById(I id);
    
    public D findById(I id);
    
    public List<D> findAllById(List<I> idList);
    
    public List<D> findAll();
    
    public D save(D d);
		
    public List<D> save(List<D> dList);
}