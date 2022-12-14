package business.service;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;
import business.util.EmailUtil;
import dtos.Paper;
import dtos.Review;
import dtos.Submission;
import dtos.SubmissionState;
import dtos.User;
import persistence.repository.SubmissionRepository;

@ApplicationScoped
public class SubmissionService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;

	public Submission getSubmission(int submissionId) { return null; }

	public void removeSubmission(Submission submission) { }

	public void changeSubmission(Submission submission, Submission newSubmission) { }

	public void setState(Submission submission, SubmissionState state) { }

	public void setEditor() { }

	public void addReviewer() { }

	public void removeReviewer() { }

	public void realeaseReview(Review review, Submission submission) { }

	public void addCoAuthor() { }

	public void uploadFile(byte[] pdf) { }

	public Paper downloadFile() { return null; }

	/*
	Repo?
	 */
	public void acceptSubmission() { }

	public void rejectSubmission() { }

	public boolean canView(Submission sub, User user) { }

	public List<Submission> getSubmissions(ScientificForum scientificForum, User role, ResultListParameters resultParams) { return null; }

	public List<Submission> getSubmissions(User role, ResultListParameters resultParams) { return null; }

	public int countSubmissions() {
		return 0;
	}
}