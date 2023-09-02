package com.tom.nhl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Players")
public class Player {

	private int id;
	private int jsonId;
	private String firstName;
	private String lastName;
	private Integer primaryNumber;
	private Integer age;
	private Date birthDate;
	private String birthCountry;
	private Team currentTeam;
	private Position position;
	
	@Id
	@SequenceGenerator(name = "playerIdGenerator", sequenceName = "SEQ_PLAYERS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playerIdGenerator")
	@Column(name = "p_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "p_jsonId")
	public int getJsonId() {
		return jsonId;
	}
	public void setJsonId(int jsonId) {
		this.jsonId = jsonId;
	}
	
	@Column(length = 25)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(length = 25)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Basic
	public Integer getPrimaryNumber() {
		return primaryNumber;
	}
	public void setPrimaryNumber(Integer primaryNumber) {
		this.primaryNumber = primaryNumber;
	}
	
	@Basic
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@Column(length = 30)
	public String getBirthCountry() {
		return birthCountry;
	}
	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currentTeamId")
	public Team getCurrentTeam() {
		return currentTeam;
	}
	public void setCurrentTeam(Team currentTeam) {
		this.currentTeam = currentTeam;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positionId")
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
