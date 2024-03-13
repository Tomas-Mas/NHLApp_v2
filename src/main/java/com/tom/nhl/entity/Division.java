package com.tom.nhl.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "divisions")
public class Division {

	private int id;
	private int jsonId;
	private String name;
	private Set<ConferenceDivision> conferences;
	
	@Id
	@SequenceGenerator(name = "divisionIdGenerator", sequenceName = "SEQ_DIVISIONS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "divisionIdGenerator")
	@Column(name = "d_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "d_jsonId")
	public int getJsonId() {
		return jsonId;
	}
	public void setJsonId(int jsonId) {
		this.jsonId = jsonId;
	}
	
	@Column(length = 15)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(mappedBy = "conferenceDivisionPk.division", fetch = FetchType.LAZY)
	public Set<ConferenceDivision> getConferences() {
		return conferences;
	}
	public void setConferences(Set<ConferenceDivision> conferences) {
		this.conferences = conferences;
	}
	
}
