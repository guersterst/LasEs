package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ProfileService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import control.internal.SessionInformation;

@RequestScoped
public class ResultListBacking {
	
	private SubmissionLieferando submissionListService;
	private ScientificForumService scientificForumService;
	private ProfileService profileService;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }

}
