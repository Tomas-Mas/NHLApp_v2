package com.tom.nhl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Conference_Division")
public class ConferenceDivision {

	private ConferenceDivisionPK conferenceDivisionPk;

	@Id
	public ConferenceDivisionPK getConferenceDivisionPk() {
		return conferenceDivisionPk;
	}
	public void setConferenceDivisionPk(ConferenceDivisionPK conferenceDivisionPk) {
		this.conferenceDivisionPk = conferenceDivisionPk;
	}
}
