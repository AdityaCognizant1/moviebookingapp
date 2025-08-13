package com.moviebookingapp.backend.config;

import com.moviebookingapp.backend.entity.Movie;
import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.entity.Theater;
import com.moviebookingapp.backend.entity.Booking;
import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.repository.MovieRepository;
import com.moviebookingapp.backend.repository.ShowtimeRepository;
import com.moviebookingapp.backend.repository.BookingRepository;
import com.moviebookingapp.backend.repository.TheaterRepository;
import com.moviebookingapp.backend.repository.UserRepository;
import com.moviebookingapp.backend.service.UserService;
import com.moviebookingapp.backend.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TheaterService theaterService;
    
    @Autowired
    private TheaterRepository theaterRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        // Load sample data in development, production (first run), or when explicitly enabled
        if (!isRunningTests() && shouldLoadSampleData()) {
            loadSampleData();
        }
    }

    /**
     * Determines if sample data should be loaded
     */
    private boolean shouldLoadSampleData() {
        // Always load if database is empty (first deployment)
        if (movieRepository.count() == 0) {
            return true;
        }
        
        // Load if explicitly enabled via environment variable
        String loadData = environment.getProperty("app.load-sample-data", "auto");
        if ("true".equalsIgnoreCase(loadData)) {
            return true;
        }
        
        // Don't load if explicitly disabled
        if ("false".equalsIgnoreCase(loadData)) {
            return false;
        }
        
        // Auto mode: load only if database is empty
        return movieRepository.count() == 0;
    }

    /**
     * Loads all sample data
     */
    private void loadSampleData() {
        System.out.println("🎬 Loading Premium Cinema sample data...");
        
        try {
            // Load in correct order due to dependencies
            theaterService.createDefaultTheaters();
            System.out.println("✅ Theaters loaded");
            
            loadSampleMovies();
            System.out.println("✅ Movies loaded");
            
            loadSampleShowtimes();
            System.out.println("✅ Showtimes loaded");
            
            loadSampleBookings();
            System.out.println("✅ Sample bookings loaded");
            
            System.out.println("🎉 Premium Cinema sample data loaded successfully!");
            System.out.println("📊 Database contains:");
            System.out.println("   - " + movieRepository.count() + " movies with high-quality posters");
            System.out.println("   - " + theaterRepository.count() + " theaters (Standard, Premium, IMAX, VIP)");
            System.out.println("   - " + showtimeRepository.count() + " showtimes across multiple theaters");
            System.out.println("   - " + userRepository.count() + " sample users");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSampleMovies() {
        Movie movie1 = Movie.builder()
            .title("Avengers: Endgame")
            .description("The epic conclusion to the Infinity Saga brings together all Marvel heroes for the ultimate battle against Thanos. Experience the most ambitious crossover event in cinematic history with stunning visual effects and emotional storytelling.")
            .genre("Action/Adventure")
            .durationMinutes(181)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg")
            .build();

        Movie movie2 = Movie.builder()
            .title("The Dark Knight")
            .description("Batman faces his greatest challenge yet as the Joker wreaks havoc on Gotham City. Christopher Nolan's masterpiece combines intense action with psychological depth in this critically acclaimed superhero thriller.")
            .genre("Action/Crime")
            .durationMinutes(152)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg")
            .build();

        Movie movie3 = Movie.builder()
            .title("Inception")
            .description("Dom Cobb is a skilled thief who enters people's dreams to steal secrets. Now he must perform the impossible task of inception - planting an idea instead of stealing one. A mind-bending journey through multiple layers of reality.")
            .genre("Sci-Fi/Thriller")
            .durationMinutes(148)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg")
            .build();

        Movie movie4 = Movie.builder()
            .title("Black Panther")
            .description("T'Challa, heir to the hidden but advanced kingdom of Wakanda, must step forward to lead his people into a new future and must confront a challenger from his country's past. A groundbreaking superhero film celebrating African culture.")
            .genre("Action/Adventure")
            .durationMinutes(134)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/uxzzxijgPIY7slzFvMotPv8wjKA.jpg")
            .build();

        Movie movie5 = Movie.builder()
            .title("Spider-Man: No Way Home")
            .description("With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. When a spell goes wrong, dangerous foes from other worlds start to appear, forcing Peter to discover what it truly means to be Spider-Man.")
            .genre("Action/Adventure")
            .durationMinutes(148)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg")
            .build();

        Movie movie6 = Movie.builder()
            .title("Interstellar")
            .description("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival. Christopher Nolan's epic space odyssey combines stunning visuals with emotional storytelling about love transcending time and space.")
            .genre("Sci-Fi/Drama")
            .durationMinutes(169)
            .rating("PG-13")
            .posterUrl("https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg")
            .build();

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);
        movieRepository.save(movie5);
        movieRepository.save(movie6);

        System.out.println("Sample movies loaded successfully!");
    }
    
    private void loadSampleShowtimes() {
        List<Movie> movies = movieRepository.findAll();
        List<Theater> theaters = theaterService.findActiveTheaters();
        LocalDateTime now = LocalDateTime.now();
        
        // Create showtimes for each movie in different theaters
        for (Movie movie : movies) {
            // Distribute movies across different theaters
            Theater theater1 = theaters.get(0); // Theater 1
            Theater theater2 = theaters.get(1); // Theater 2
            Theater imaxTheater = theaters.get(2); // IMAX Screen
            Theater vipTheater = theaters.get(3); // VIP Hall
            
            // Today's showtimes - different theaters
            createShowtime(movie, theater1, now.withHour(14).withMinute(30));
            createShowtime(movie, theater2, now.withHour(17).withMinute(45));
            createShowtime(movie, imaxTheater, now.withHour(21).withMinute(0));
            
            // Tomorrow's showtimes - spread across theaters
            LocalDateTime tomorrow = now.plusDays(1);
            createShowtime(movie, theater1, tomorrow.withHour(12).withMinute(0));
            createShowtime(movie, theater2, tomorrow.withHour(15).withMinute(15));
            createShowtime(movie, vipTheater, tomorrow.withHour(18).withMinute(30));
            createShowtime(movie, imaxTheater, tomorrow.withHour(21).withMinute(45));
            
            // Day after tomorrow's showtimes
            LocalDateTime dayAfter = now.plusDays(2);
            createShowtime(movie, theater1, dayAfter.withHour(13).withMinute(30));
            createShowtime(movie, theater2, dayAfter.withHour(16).withMinute(45));
            createShowtime(movie, vipTheater, dayAfter.withHour(20).withMinute(0));
        }
        
        System.out.println("Sample showtimes loaded successfully!");
    }
    
    private void createShowtime(Movie movie, Theater theater, LocalDateTime showTime) {
        Showtime showtime = Showtime.builder()
                .movie(movie)
                .theater(theater)
                .showTime(showTime)
                .build();
        showtimeRepository.save(showtime);
    }
    
    private void loadSampleBookings() {
        // Create a sample regular user for bookings
        User sampleUser = User.builder()
                .username("sampleuser")
                .password("password123")
                .email("sample@example.com")
                .role("USER")  // Regular user
                .build();
        
        User savedUser = userService.save(sampleUser);
        
        // Create a sample admin user
        User adminUser = User.builder()
                .username("admin")
                .password("admin123")
                .email("admin@example.com")
                .role("ADMIN")  // Admin user
                .build();
        
        userService.save(adminUser);
        
        // Get some showtimes to create bookings for
        List<Showtime> showtimes = showtimeRepository.findAll();
        
        if (!showtimes.isEmpty()) {
            // Create a few sample bookings
            for (int i = 0; i < Math.min(3, showtimes.size()); i++) {
                Booking booking = Booking.builder()
                        .user(savedUser)
                        .showtime(showtimes.get(i))
                        .numTickets(2 + i) // 2, 3, 4 tickets
                        .bookingTime(LocalDateTime.now().minusDays(i))
                        .build();
                
                bookingRepository.save(booking);
                
                // Update available seats
                Showtime showtime = showtimes.get(i);
                showtime.setAvailableSeats(showtime.getAvailableSeats() - booking.getNumTickets());
                showtimeRepository.save(showtime);
            }
        }
        
        System.out.println("Sample bookings loaded successfully!");
    }
    
    private boolean isRunningTests() {
        // Check if we're running in a test environment
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("junit") || 
                element.getClassName().contains("surefire") ||
                element.getClassName().contains("Test")) {
                return true;
            }
        }
        return false;
    }
}
