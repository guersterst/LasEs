package dtos;

import java.util.List;

public abstract class ScientificForum {
	
	private int id;
	
	private String description;
	
	private String reviewManual;
	
	private String url;
	
	private List<ScienceField> topics;
	
	private List<User> editors;

}
