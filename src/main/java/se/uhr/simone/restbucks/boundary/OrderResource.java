package se.uhr.simone.restbucks.boundary;

import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import se.uhr.nya.integration.sim.extension.api.feed.UniqueIdentifier;
import se.uhr.simone.restbucks.control.OrderController;

@Api(tags = {"example"})
@Path("order")
@Singleton
public class OrderResource {

	@Inject
	private OrderController controller;
	
	@ApiOperation(value = "Create order", response = OrderRepresentation.class)
	@POST
	public Response create(String description) {
		OrderRepresentation order = controller.create(description);
		return Response.ok(order).build();
	}
	

	@ApiOperation(value = "Fetch all orders", response = OrderRepresentation.class, responseContainer = "List")
	@GET
	public Response readAll() {
		List<OrderRepresentation> orders = controller.getAll();
		
		return Response.ok(orders).build();
	}
	
	@ApiOperation(value = "Fetch order", response = OrderRepresentation.class)
	@GET
	@Path("/{orderId}")
	public Response read(@PathParam("orderId") String orderId) {
		UniqueIdentifier id = UniqueIdentifier.of(orderId);
		
		OrderRepresentation order = controller.get(id);
		
		return order != null ? Response.ok(order).build() : Response.status(Status.NOT_FOUND).build();
	}
}
