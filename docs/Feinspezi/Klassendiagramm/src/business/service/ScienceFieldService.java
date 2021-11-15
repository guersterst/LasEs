package business.service;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;
import dtos.ScienceField;
import persistence.repository.ScienceFieldRepository;

@ApplicationScoped
public class ScienceFieldService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;
	
	public void add(ScienceField field) { }

	public boolean exists(ScienceField scienceField) { }
	
	public void remove(ScienceField field) { }
	
	public List<ScienceField> getScienceFields() { return null; }
}
