package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.authentication.AuthProvider;

@Component
public class JdbcSubjectDao implements SubjectDao {

	private JdbcTemplate jdbcTemplate;
	
	  @Autowired
	    private AuthProvider authProvider;
	
	@Autowired
	public JdbcSubjectDao (BasicDataSource dataSource) {
		jdbcTemplate = new JdbcTemplate (dataSource);
	}
	
	@Override
	public Subject createSubject(Subject subject) {
		
		String sql = "INSERT INTO subjects (user_id, subject_name, subject_icon_path) "
				+ "VALUES(?, ?, ?) RETURNING subject_id;";
		 SqlRowSet results = jdbcTemplate.queryForRowSet(sql, authProvider.getCurrentUser().getId(), subject.getSubjectName(), subject.getIconPath());
		
		 if (results.next()) {
			 subject.setSubjectId(results.getLong("subject_id"));
		 }
		return subject;
	}

	@Override
	public Subject updateSubject(Subject subject) {
		String sql = "update subjects SET subject_name = ? "
				+ "WHERE subject_id = ? "
				+ "RETURNING subject_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, subject.getSubjectName(), subject.getSubjectId());
		if (results.next()) {
			subject.setUserId(results.getLong("subject_id"));
		}
		return subject;
		
	}

	@Override
	public void updateIcon(String iconPath, long subjectId) {
		jdbcTemplate.update("update subjects SET subject_icon_path = ?"
				+ " WHERE subject_id = ?);", iconPath, subjectId);
		
	}

	// users can view their subjects only
	@Override
	public List<Subject> getUserSubjects() {
		List<Subject> allSubjects = new ArrayList<Subject>();
		String sqlAllSubjects = "SELECT subject_id, user_id, subject_name, subject_icon_path "
				+ "from subjects "
				+ "JOIN users on subjects.user_id = users.id "
				+ "WHERE id = ? "
				+ "ORDER BY subject_id;";
		
		SqlRowSet results= jdbcTemplate.queryForRowSet(sqlAllSubjects, authProvider.getCurrentUser().getId());
		while(results.next()) {
			allSubjects.add(mapRowToSubject(results));
		}
		return allSubjects;
	}

	@Override
	public void deleteSubject(long subjectId) {
		jdbcTemplate.update("DELETE FROM subjects WHERE subject_id = ?;", subjectId);
		
	}
	
	
	// admins can view all subjects from all users
	@Override
	public List<Subject> getAllSubjects() {
		List<Subject> allSubjects = new ArrayList<Subject>();
		String sqlAllSubjects = "SELECT subject_id, user_id, subject_name, subject_icon_path "
				+ "FROM subjects ORDER BY subject_id;";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sqlAllSubjects);
		while(results.next()) {
			allSubjects.add(mapRowToSubject(results));
		}
		return allSubjects;
	}
	
	private Subject mapRowToSubject (SqlRowSet results) {
		Subject subject = new Subject ();
		subject.setSubjectId(results.getLong("subject_id"));
		subject.setUserId(results.getLong("user_id"));
		subject.setSubjectName(results.getString("subject_name"));
		subject.setIconPath(results.getString("subject_icon_path"));
		
		return subject;
	}

}
