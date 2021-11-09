package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import dtos.ScientificForum;

@RequestScoped
public class NewScientificForumBacking {
	
	private ScienceFieldService scienceFieldBacking;
	
	private ScientificForumService scientificForumService;

	private ScientificForum newScientificForum;
	
	
	@PostConstruct
	public void init() { }

	public void addEditor() { }

	public void removeEditor() { }

	public void addScienceFieldToForum() { }

	public void addScienceField() { }

	public void create() { }

}
