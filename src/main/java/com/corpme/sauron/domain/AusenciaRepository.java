package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Ausencia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AusenciaRepository extends CrudRepository<Ausencia, Long> {

}
