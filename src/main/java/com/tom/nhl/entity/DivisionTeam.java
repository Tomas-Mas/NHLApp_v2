package com.tom.nhl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "division_teams")
public class DivisionTeam {

	private DivisionPK divisionPk;

	@Id
	public DivisionPK getDivisionPk() {
		return divisionPk;
	}
	public void setDivisionPk(DivisionPK divisionPk) {
		this.divisionPk = divisionPk;
	}
}
