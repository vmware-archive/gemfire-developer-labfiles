package io.pivotal.bookshop.keys;

import java.io.Serializable;

//TODO-01a: Open this key class and observe the components of the key. 
public class OrderKey implements Serializable	
{
	private static final long serialVersionUID = 1L;
	private Integer customerNumber;
	private Integer orderNumber;
	
	public OrderKey(Integer custNum, Integer orderNumber)
	{
		this.setOrderNumber(orderNumber);
		this.customerNumber = custNum;
	}
	
	public Integer getCustomerNumber()
	{
		return customerNumber;
	}

	public void setCustomerNumber(Integer customerNumber)
	{
		this.customerNumber = customerNumber;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	// TODO-01b: Notice the implementation of hashCode() and equals(). The customerId attribute is only used for partitioning while
	//           the orderNumber is used for equality and hashCode.
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result
				+ ((orderNumber == null) ? 0 : orderNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderKey other = (OrderKey) obj;
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderKey [customerNumber=" + customerNumber + ", orderNumber="
				+ orderNumber + "]";
	}


}
