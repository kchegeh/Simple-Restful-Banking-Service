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
import javax.persistence.Temporal;

import org.joda.time.LocalDate;

/**
 * Created by Simon on 14/02/2017.
 */
@Entity
@Table(name = "Accounts_Deposits")
public class AccountDeposit extends ModelBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "Deposited_Amount", nullable = false)
	private Long depositedAmount;

	@Column(name = "Date_Deposited", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date dateDeposited;

	@ManyToOne
	@JoinColumn(name = "Account_Id", nullable = false)
	private BankAccount ownerBankAccount;

	public AccountDeposit()
	{

	}

	public AccountDeposit(BankAccount bankAccount, Long amount)
	{
		this.ownerBankAccount = bankAccount;
		this.depositedAmount = amount;
		this.dateDeposited = LocalDate.now().toDate();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getDepositedAmount()
	{
		return depositedAmount;
	}

	public void setDepositedAmount(Long amount)
	{
		this.depositedAmount = amount;
	}

	public Date getDateDeposited()
	{
		return dateDeposited;
	}

	public void setDateDeposited(Date date)
	{
		this.dateDeposited = date;
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
		return String.format("AC/DEPOSIT-%d/%d", id, depositedAmount);
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 != null && arg0 instanceof AccountDeposit)
		{
			return this.hashCode() == ((AccountDeposit) arg0).hashCode();
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
