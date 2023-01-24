package tse.fise2.image3.cardmatcher.model;

/**
 * The ScoreImage class represents an image with a name and its score of proximity.
*/

public class ScoreImage {
    String imageName;
    Double score;

    /**
     * Creates a new ScoreImage object with the given image name and score.
     * @param imageName the name of the image
     * @param score the score of the image
    */
    public ScoreImage(String imageName, Double score) {
    this.imageName = imageName;
    this.score = score;
    }
    /**
     * Returns the name of the image.
     * @return the name of the image
    */
    public String getImageName() {
    return imageName;
    }
    /**
     * Sets the name of the image.
     * @param imageName the new name of the image
    */
    public void setImageName(String imageName) {
    this.imageName = imageName;
    }
    /**
     * Returns the score of the image.
     * @return the score of the image
    */
    public Double getScore() {
    return score;
    }
    /**
     * Sets the score of the image.
     * @param score the new score of the image
    */
    public void setScore(Double score) {
    this.score = score;
    }
    }