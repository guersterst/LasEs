package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import control.internal.SessionInformation;
import dtos.ScienceField;
import dtos.ScientificForum;
import dtos.User;

@RequestScoped
public class NewScientificForumBacking {
	
	private ScienceFieldService scienceFieldService;
	
	private ScientificForumService scientificForumService;

	private ScientificForum newScientificForum;
	
	private SessionInformation sessionInformation;
	
	private String editorEmailInput;
	
	private List<User> editors;
	
	private List<ScienceField> scienceFields;
	
	private List<ScienceField> selectedScienceFields;
	
	private ScienceField scienceFieldSelectionInput;
	
	private String newScienceFieldInput;
	
	
	@PostConstruct
	public void init() { }

	public void addEditor() { }

	public void removeEditor(int id) { }

	public void addNewScienceField() { }

	public void addScienceField() { }

	public void create() { }
	
	public void abort() { }

}
