package org.tala.bank.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tala.bank.model.AccountWithdrawal;

/**
 * Created by Simon on 14/02/2017.
 */
public interface AccountWithdrawalsRepository extends CrudRepository<AccountWithdrawal, Long>
{

	@Query(value = "SELECT * FROM Accounts_Withdrawals WHERE Account_Id = ?1 and Withdrawal_Date = ?2", nativeQuery = true)
	List<AccountWithdrawal> findByBankAccountAndDate(Long accountId, Date date);
}
