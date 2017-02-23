package org.tala.bank.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tala.bank.model.AccountDeposit;

/**
 * Created by Simon on 14/02/2017.
 */
public interface AccountDepositsRepository extends CrudRepository<AccountDeposit, Long>
{

	@Query(value = "SELECT * FROM Accounts_Deposits WHERE Account_Id = ?1 and Date_Deposited = ?2", nativeQuery = true)
	List<AccountDeposit> getAccountDepositsByAccountAndDate(Long accountId, Date date);

	@Query(value = "SELECT SUM(Deposited_Amount) FROM Accounts_Deposits WHERE Account_Id = ?1 and Date_Deposited = ?2", nativeQuery = true)
	Long getTotalAccountDepositsByAccountAndDate(Long accountId, Date date);
}
