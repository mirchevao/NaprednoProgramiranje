import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MovieTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
class Movie {
    String title;
    List<Integer> ratings;

    public Movie(String title, int[] ratings ) {
        this.title = title;
        this.ratings = new ArrayList<>();
    }
    public String getTitle(){
        return title;
    }

    public List<Integer> getRatings(){
        return ratings;
    }
    public double avgRaiting() {
        double sum =0;
        for(int i=0; i<ratings.size(); i++ ) {
            sum+=ratings.get(i);
        }
        return sum/ratings.size();
    }
    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d raitings", title, avgRaiting(), ratings.size());
    }
}
class MoviesList {
    List<Movie> movies;
    private double se;

    public MoviesList(List<Movie> movies) {
        movies = new ArrayList<>();
    }

    public MoviesList() {

    }

    public void addMovie(String title, int[] raitings) {
        Movie m = new Movie(title,raitings);
        movies.add(m);
    }
    public List<Movie> top10ByAvgRating() {
        List<Movie> list = movies.stream().sorted(Comparator.comparing(Movie::avgRaiting).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
        return list;
    }
    public List<Movie> top10ByRatingCoef() {
        int max = movies.stream().mapToInt(movie->movie.getRatings().size()).max().getAsInt();
        Comparator<Movie> comparator = (o1,o2)-> {
            double first = o1.avgRaiting() * o1.getRatings().size()/max;
            double second = o2.avgRaiting() * o2.getRatings().size()/max;
            if(Double.compare(se,first)==0)
                return o1.getTitle().compareTo(o2.getTitle());
            return Double.compare(second, first);
        };
        List<Movie> list = movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());
        return list;
    }


}
