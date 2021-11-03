package dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ScientificForum {
	
	private int id;
	
	private String description;
	
	private String reviewManual;
	
	private String url;
	
	private List<ScienceField> topics;
	
	private List<User> editors;
	
	private LocalDateTime deadline;

}
