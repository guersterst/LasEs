package control.backing;

import java.time.LocalDate;
import java.util.List;

import business.service.ScientificForumService;
import dtos.ScienceField;
import dtos.ScientificForum;
import idk.SessionInformation;

@RequestScoped
public class ScientificForumListBacking {

	private SessionInformation sessionInformation;
	
	private ScientificForumService scientificService;

	private LocalDate dateDeadlineBeforeFilter;

	private LocalDate dateDeadlineAfterFilter;

	private String searchString;

	public void applyFilters(){}

	public void sortNameUp(){}

	public void sortNameDown(){}

	public void sortDeadlineUp(){}

	public void sortDealineDown(){}

	public List<ScienceField> getScienceFields() {return null; }
	
	public List<ScientificForum> getScientificForums() { return null; }





}
