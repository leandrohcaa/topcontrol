package com.topcontrol.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.beans.BeanUtils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Data
public abstract class BaseEntity<I extends Serializable> implements Serializable {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected Long id;

	public BaseEntity() {
		super();
	}

	public BaseEntity(Long id) {
		super();
		this.id = id;
	}

	@SuppressWarnings("rawtypes")
	public BaseEntity clone() {
		BaseEntity result = null;
		try {
			result = (this.getClass()).getConstructor().newInstance();
			BeanUtils.copyProperties(this, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
