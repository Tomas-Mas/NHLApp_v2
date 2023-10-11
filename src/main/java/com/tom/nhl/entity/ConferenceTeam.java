package com.tom.nhl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Conference_teams")
public class ConferenceTeam {

	private ConferencePK conferencePk;

	@Id
	public ConferencePK getConferencePk() {
		return conferencePk;
	}
	public void setConferencePk(ConferencePK conferencePk) {
		this.conferencePk = conferencePk;
	}
}
