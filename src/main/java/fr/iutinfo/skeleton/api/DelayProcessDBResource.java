package fr.iutinfo.skeleton.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import delay.DelayProcess;

@Path("/delaydb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DelayProcessDBResource {

	public DelayProcessDBResource() {
	}

	@GET
	public String getTravelTime() {
		return "GET REQUEST";
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String newTodo(@FormParam("date") String date, @FormParam("combo") String busId, @FormParam("locationcombo") String location, @FormParam("time") String time){
		DelayProcess delay = new DelayProcess(date, busId, location, time);
		delay.process();
		return ("<h1>Delay : "+delay.getDelay().toHours()+" Hours "+(delay.getDelay().toMinutes()%60)+ "Minutes "+(delay.getDelay().getSeconds()%60)%60+" Secondes</h1>");
	}

}
