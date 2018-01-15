package gamestudio.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
create table comment (
ident integer primary key,
username varchar(32) not null,
game varchar(32) not null,
contant varchar(128) not null,
createdOn timestamp not null)
*/
@Entity
public class Comment {
	@Id
	@GeneratedValue
	private int ident;

	private String username;

	private String game;

	private String content;

	private Date createdOn;

	public Comment() {
		super();
	}

	public Comment(String username, String game, String content, Date createdOn) {
		super();
		this.username = username;
		this.game = game;
		this.content = content;
		this.createdOn = createdOn;
	}

	public int getIdent() {
		return ident;
	}

	public void setIdent(int ident) {
		this.ident = ident;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return String.format("Comment  %d  %s  %s  %d", ident, username, game, content, createdOn);
	}
}
