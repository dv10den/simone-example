package se.uhr.simone.restbucks.entity;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.Instant;

import org.junit.Test;

import se.uhr.simone.example.api.OrderRepresentation;

public class OrderRepositoryTest {

	private OrderRepository cut = new OrderRepository();

	private static final Instant EPOC = Instant.ofEpochSecond(0);

	private static final OrderRepresentation ORDER = OrderRepresentation.builder().withId("myid")
			.withDescription("desc").withTime(EPOC).build();

	@Test
	public void shouldConvertToXml() throws Exception {
		String xml = cut.convertToXml(ORDER);
		assertThat(xml, containsString("myid"));
		assertThat(xml, containsString("1970-01-01T00:00:00Z"));
	}

	@Test
	public void shouldConvertFromXml() throws Exception {
		String xml = cut.convertToXml(ORDER);
		OrderRepresentation order = cut.convertFromXml(xml);
		assertThat(order.getId(), is("myid"));
		assertThat(order.getTime(), is(EPOC));
	}
}
