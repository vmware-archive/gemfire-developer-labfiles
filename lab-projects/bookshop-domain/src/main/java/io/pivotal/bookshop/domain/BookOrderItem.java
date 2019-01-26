package io.pivotal.bookshop.domain;

import java.io.Serializable;

public class BookOrderItem implements Serializable
{
	private static final long serialVersionUID = 7526471155622776147L;

	private int orderLine;
	private Integer itemNumber;
	private int quantity;
	private float discount;
	
	
	
	public BookOrderItem(int orderLine, Integer itemNumber, int quantity,
			float discount)
	{
		super();
		this.orderLine = orderLine;
		this.itemNumber = itemNumber;
		this.quantity = quantity;
		this.discount = discount;
	}
	public int getOrderLine()
	{
		return orderLine;
	}
	public void setOrderLine(int orderLine)
	{
		this.orderLine = orderLine;
	}
	public Integer getItemNumber()
	{
		return itemNumber;
	}
	public void setItemNumber(Integer itemNumber)
	{
		this.itemNumber = itemNumber;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public float getDiscount()
	{
		return discount;
	}
	public void setDiscount(float discount)
	{
		this.discount = discount;
	}
	@Override
	public String toString()
	{
		return "BookOrderItem [orderLine=" + orderLine + ", itemNumber="
				+ itemNumber + ", quantity=" + quantity + ", discount="
				+ discount + "]";
	}
}
