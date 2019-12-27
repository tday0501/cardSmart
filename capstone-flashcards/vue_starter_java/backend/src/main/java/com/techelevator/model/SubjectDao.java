package com.techelevator.model;

import java.util.List;

public interface SubjectDao {
	
	public Subject createSubject(Subject subject); 
	
	public void deleteSubject(long subjectId);
	
	public Subject updateSubject(Subject subject);
	
	public void updateIcon(String iconPath, long subjectId);
	
	public List<Subject> getUserSubjects();
	
	public List <Subject> getAllSubjects ();

}
