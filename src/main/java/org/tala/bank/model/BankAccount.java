package org.tala.bank.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Created by Simon on 14/02/2017.
 * 
 * Represents an instance a Bank Account a customer can operate with the bank.
 */
@Entity
@Table(name = "Bank_Accounts")
public class BankAccount extends ModelBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "Account_Name", nullable = false, length = 255, unique = true)
	private String accountName;

	@Column(name = "Balance", nullable = false)
	private Long balance;

	@Column(name = "Last_Transaction_Date", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastTransactionDate;

	@Column(name = "Start_Date", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date startDate = new Date();

	@Column(name = "End_Date", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date endDate;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "ownerBankAccount")
	private Set<AccountDeposit> ownedAccountDeposits;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "ownerBankAccount")
	private Set<AccountWithdrawal> ownedAccountWithdrawals;

	public BankAccount()
	{
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public BankAccount(Long balance)
	{
		this.balance = balance;
	}

	public Date getLastTransactionDate()
	{
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate)
	{
		this.lastTransactionDate = lastTransactionDate;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public Long getBalance()
	{
		return balance;
	}

	public void setBalance(Long balance)
	{
		this.balance = balance;
	}

	public Set<AccountDeposit> getOwnedAccountDeposits()
	{
		return ownedAccountDeposits;
	}

	public void setOwnedAccountDeposits(Set<AccountDeposit> ownedAccountDeposits)
	{
		this.ownedAccountDeposits = ownedAccountDeposits;
	}

	public Set<AccountWithdrawal> getOwnedAccountWithdrawals()
	{
		return ownedAccountWithdrawals;
	}

	public void setOwnedAccountWithdrawals(Set<AccountWithdrawal> ownedAccountWithdrawals)
	{
		this.ownedAccountWithdrawals = ownedAccountWithdrawals;
	}

	@Override
	public String toString()
	{
		return String.format("AC/%d-%s", id, accountName);
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 != null && arg0 instanceof BankAccount)
		{
			return this.hashCode() == ((BankAccount) arg0).hashCode();
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}

}
