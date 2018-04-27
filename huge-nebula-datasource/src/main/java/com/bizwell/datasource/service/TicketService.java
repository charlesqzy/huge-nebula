package com.bizwell.datasource.service;

import com.bizwell.datasource.bean.domain.Ticket;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public interface TicketService {

	public String storeTicke(Ticket ticket);
	
	public Ticket validateTicket(String ticketCode);
	
	public void removeTicket(String ticketCode);
}
