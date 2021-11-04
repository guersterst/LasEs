package control.backing;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import dtos.ScientificForum;
import idk.SessionInformation;

public class ScientificForumBacking {

	private SessionInformation sessionInformation;
	
	private SubmissionLieferando submissionListService;
	
	private ScientificForumService scientificForumService;
	
	private ScientificForum scientificForum;
	
	private ScienceFieldService scienceFieldService;
	
	public ScientificForum getScientificForum() { return null; }

}
