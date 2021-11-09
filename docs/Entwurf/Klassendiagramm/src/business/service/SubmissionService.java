package business.service;

import java.util.List;

import business.util.EmailUtil;
import dtos.Paper;
import dtos.Review;
import dtos.Submission;
import dtos.SubmissionState;
import dtos.User;
import persistence.repository.SubmissionRepository;

public class SubmissionService {

	private ExceptionQueue exceptionQueue;

	private EmailUtil emailUtil;

	private PaperRepository paperRepository;
	
	public Submission getSubmission(int submissionId) { return null; }
	
	public void removeSubmission(Submission submission) { }

	public void changeSubmission(Submission submission, Submission newSubmission) { }
	
	public void setState(Submission submission, SubmissionState state) { }

	public void uploadPaper(byte[] pdf) { }

	public Paper downloadPaper() { return null; }

	public List<Submission> getSubmissions(int forumId, String userEmail, ResultListParameters resultParams) { return null; }

	public List<Submission> getSubmissions(String searchString, String userEmail, ResultListParameters resultParams) { return null; }

	public List<Submission> getSubmissions(String userEmail, Privilege role, ResultListParameters resultParams) { return null;


}
