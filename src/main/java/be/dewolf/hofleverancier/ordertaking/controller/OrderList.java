package be.dewolf.hofleverancier.ordertaking.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderList {

	private List<OrderDTO> orders;

	@JsonCreator
	public OrderList(@JsonProperty("orders") List<OrderDTO> orders) {
		this.orders = orders;
	}

	public List<OrderDTO> getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		return "OrderList{" +
				"orders=" + orders +
				'}';
	}
}
