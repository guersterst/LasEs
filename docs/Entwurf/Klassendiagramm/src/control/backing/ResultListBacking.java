package control.backing;

import javax.annotation.PostConstruct;

import business.internal.SessionInformation;
import business.service.ProfileService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;

public class ResultListBacking {
	
	private SubmissionLieferando submissionListService;
	private ScientificForumService scientificForumService;
	private ProfileService profileService;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }

}
