package se.uhr.simone.restbucks.control;

import java.io.StringWriter;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import se.uhr.nya.integration.sim.extension.api.feed.AtomCategory;
import se.uhr.nya.integration.sim.extension.api.feed.AtomCategory.Label;
import se.uhr.nya.integration.sim.extension.api.feed.AtomCategory.Term;
import se.uhr.nya.integration.sim.extension.api.feed.AtomEntry;
import se.uhr.nya.integration.sim.extension.api.feed.AtomEntry.AtomEntryId;
import se.uhr.nya.integration.sim.extension.api.feed.FeedPublisher;
import se.uhr.nya.integration.sim.extension.api.feed.UniqueIdentifier;
import se.uhr.simone.restbucks.boundary.OrderRepresentation;
import se.uhr.simone.restbucks.enity.OrderRepository;

public class OrderController {

	@Inject
	private OrderRepository orderRepository;

	@Inject
	private FeedPublisher feedPublisher;

	public OrderRepresentation create(String description) {
		UniqueIdentifier orderId = UniqueIdentifier.randomUniqueIdentifier();

		OrderRepresentation order = OrderRepresentation.of(orderId.toString(), description, LocalDateTime.now());

		orderRepository.create(orderId, order);

		publishFeedEntry(orderId, convertToXml(order));

		return order;
	}

	public OrderRepresentation read(UniqueIdentifier id) {
		return orderRepository.read(id);
	}

	private String convertToXml(OrderRepresentation order) {
		try {
			JAXBContext context = JAXBContext.newInstance(order.getClass());
			Marshaller m = context.createMarshaller();
			StringWriter outstr = new StringWriter();
			m.marshal(order, outstr);
			return outstr.toString();
		} catch (JAXBException e) {
			throw new OrderException("Can't create event", e);
		}
	}

	private void publishFeedEntry(UniqueIdentifier uid, String content) {
		AtomEntry entry = AtomEntry.builder().withAtomEntryId(AtomEntryId.of(uid, MediaType.APPLICATION_XML))
				.withSubmittedNow().withXml(content)
				.withCategory(AtomCategory.of(Term.of("myterm"), Label.of("mylabel"))).build();

		feedPublisher.publish(entry);
	}
}
