package business.service;

import java.util.List;

import dtos.ScienceField;
import persistence.repository.ScienceFieldRepository;

public class ScienceFieldService {

	private ExceptionQueue exceptionQueue;

	private ScienceFieldRepository scienceFieldRepository;
	
	public void add(ScienceField field) { }

	public boolean exists() { }
	
	public void remove(ScienceField field) { }
	
	public List<ScienceField> getScienceFields() { return null; }
}
