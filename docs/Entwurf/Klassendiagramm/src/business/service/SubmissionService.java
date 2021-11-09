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
	
	public Submission getSubmission(int submissionId) { return null; }
	
	public void removeSubmission(Submission submission) { }
	
	public void addRevision(Submission submission, Paper revision) {} 
	
	public void addEditor(User user) { }
	
	public void removeEditor(User user) { }
	
	public List<User> getEditors() { return null; }
	
	public void uploadPDF(byte[] pdf) { }
	
	public void unlockRevision(int paperId) { }
	
	public void changeSubmission(int submissionId, Submission newSubmission) { }
	
	public void setState(Submission submission, SubmissionState state) { }

	public List<Submission> getSubmissionsForForum(int forumId, String userEmail, ResultListParameters resultParams) { return null; }

	public List<Submission> getSubmissionsWithSearchword(String searchString, String userEmail, ResultListParameters resultParams) { return null; }

	public List<Submission> getSubmissionsForHomepage(String userEmail, Privilege role, ResultListParameters resultParams) { return null; }

}
