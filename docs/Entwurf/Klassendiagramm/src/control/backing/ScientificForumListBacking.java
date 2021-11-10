package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.ScientificForumService;
import control.internal.SessionInformation;
import dtos.LocalDate;
import dtos.Pagination;
import dtos.ScienceField;
import dtos.ScientificForum;

@ViewScoped
public class ScientificForumListBacking {

	private SessionInformation sessionInformation;
	
	private Pagination<ScientificForum> scientificForumPagination;
	
	private ScientificForumService scientificService;

	private LocalDate dateDeadlineBeforeFilter;

	private LocalDate dateDeadlineAfterFilter;

	private String searchString;
	
	@PostConstruct
	public void init() { }

	public void applyFilters(){}

	public void sortNameUp(){}

	public void sortNameDown(){}

	public void sortDeadlineUp(){}

	public void sortDealineDown(){}

	public List<ScienceField> getScienceFields() {return null; }
	
	public List<ScientificForum> getScientificForums() { return null; }





}
