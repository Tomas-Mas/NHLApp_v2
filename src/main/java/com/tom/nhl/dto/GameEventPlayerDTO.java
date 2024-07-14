package com.tom.nhl.dto;

public class GameEventPlayerDTO {

	private int id;
	private String firstName;
	private String lastName;
	private int number;
	private String role;
	
	public GameEventPlayerDTO(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public GameEventPlayerDTO(int id, String firstName, String lastName, int number, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
		this.role = role;
	}
	
	public GameEventPlayerDTO(GameEventPlayerDTO player) {
		this.id = player.getId();
		this.firstName = player.getFirstName();
		this.lastName = player.getLastName();
		this.number = player.getNumber();
		this.role = player.getRole();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
