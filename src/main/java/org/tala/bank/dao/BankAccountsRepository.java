package org.tala.bank.dao;

import org.springframework.data.repository.CrudRepository;
import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */
public interface BankAccountsRepository extends CrudRepository<BankAccount, Long>
{
}
