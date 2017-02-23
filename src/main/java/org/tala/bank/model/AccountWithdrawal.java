package org.tala.bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;

/**
 * Created by Simon on 14/02/2017.
 */
@Entity
@Table(name = "Accounts_Withdrawals")
public class AccountWithdrawal extends ModelBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "Withdrawal_Amount", nullable = false)
	private Long withdrawalAmount;

	@Column(name = "Withdrawal_Date", nullable = false)
	private Date withdrawalDate;

	@ManyToOne
	@JoinColumn(name = "Account_Id", nullable = false)
	private BankAccount ownerBankAccount;

	public AccountWithdrawal()
	{
	}

	public AccountWithdrawal(BankAccount bankAccount, Long amount)
	{
		this.ownerBankAccount = bankAccount;
		this.withdrawalAmount = amount;
		this.withdrawalDate = LocalDate.now().toDate();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getWithdrawalAmount()
	{
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(Long amount)
	{
		this.withdrawalAmount = amount;
	}

	public Date getWithdrawalDate()
	{
		return withdrawalDate;
	}

	public void setWithdrawalDate(Date date)
	{
		this.withdrawalDate = date;
	}

	public BankAccount getOwnerBankAccount()
	{
		return ownerBankAccount;
	}

	public void setOwnerBankAccount(BankAccount ownerBankAccount)
	{
		this.ownerBankAccount = ownerBankAccount;
	}

	@Override
	public String toString()
	{
		return String.format("AC/WITHDRAWAL-%d/%d", id, withdrawalAmount);
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 != null && arg0 instanceof AccountWithdrawal)
		{
			return this.hashCode() == ((AccountWithdrawal) arg0).hashCode();
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
