package application;

import javafx.scene.image.Image;

public class Review {
	private int id;
	private String title;
	private String author;
	private String animalName;
	private String writeDate;
	private String content;
	private int likes;
	private boolean isBest;
	private Image image1;
	private Image image2;

	public Review(int id, String title, String author, String animalName, String writeDate, String content, int likes,
			boolean isBest, Image image1, Image image2) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.animalName = animalName;
		this.writeDate = writeDate;
		this.content = content;
		this.likes = likes;
		this.isBest = isBest;
		this.image1 = image1;
		this.image2 = image2;
	}

	// Getter methods
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getAnimalName() {
		return animalName;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public String getContent() {
		return content;
	}

	public int getLikes() {
		return likes;
	}

	public boolean isBest() {
		return isBest;
	}

	public Image getImage1() {
		return image1;
	}

	public Image getImage2() {
		return image2;
	}
}