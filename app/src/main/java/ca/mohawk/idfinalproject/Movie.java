package ca.mohawk.idfinalproject;
// Movie class contains getters and setters for movie object
public class Movie {

    public Movie(String title, String year, String image, String id) {
        this.setTitle(title);
        this.setYear(year);
        this.setImage(image);
        this.setId(id);
    }

    public Movie() {
        //  this.setImage(image);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String title;
    private String year;
    private String image;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
