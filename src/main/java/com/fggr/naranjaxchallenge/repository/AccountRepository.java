package com.fggr.naranjaxchallenge.repository;

import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dto.AccountDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends CrudRepository<AccountDao, Integer> {

    List<AccountDao> findByDniAndName(Integer dni, String name);

    List<AccountDao> findByIdEquals(Integer id);

}
