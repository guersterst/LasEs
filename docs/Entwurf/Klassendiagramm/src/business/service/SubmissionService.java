package business.service;

import java.util.List;

import business.util.EmailService;
import dtos.Paper;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.SubmissionRepository;

public class SubmissionService {
	
	private EmailService emailUtil;
	
	private SubmissionRepository submissionRepository;
	
	public Submission getSubmission(int submissionId) { return null; }
	
	public void removeSubmission(Submission submission) { }
	
	public void addRevision(Submission submission, Paper revision) {} 
	
	public void addEditor(User user) { }
	
	public void removeEditor(User user) { }
	
	public List<User> getEditors() { return null; }
	
	public void uploadPDF(byte[] pdf) { }
	
	public void unlockRevision(int paperId) { }
	
	public void changeSubmission(Submission submission, Submission newSubmission) { }

}
