package com.topcontrol.repository.base;

import com.topcontrol.domain.base.*;
import com.topcontrol.domain.*;
import org.springframework.data.jpa.repository.*;
import java.io.*;

public interface BaseRepository<D extends BaseEntity<I>, I extends Serializable> extends JpaRepository<D, I> {

}
