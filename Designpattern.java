        DESIGN PATTERN ASSIGNMENTS
A. Structural Patterns
1. Adapter
The Adapter Pattern is used to bridge two incompatible interfaces, allowing classes to work together that otherwise could not due to incompatible interfaces.
Use Case: Suppose we have a media player that only plays MP3 files, but we want it to also support MP4 and VLC formats.
// Step 1: Create MediaPlayer and AdvancedMediaPlayer interfaces
interface MediaPlayer {
    void play(String audioType, String fileName);
}
interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}
// Step 2: Implement AdvancedMediaPlayer for VLC and MP4
class VlcPlayer implements AdvancedMediaPlayer {
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: " + fileName);
    }
    public void playMp4(String fileName) {}
}

class Mp4Player implements AdvancedMediaPlayer {
    public void playVlc(String fileName) {}
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: " + fileName);
    }
}
// Step 3: Create Adapter implementing MediaPlayer
class MediaAdapter implements MediaPlayer {
    AdvancedMediaPlayer advancedMusicPlayer;
    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}
// Step 4: Implement MediaPlayer in AudioPlayer
class AudioPlayer implements MediaPlayer {
    MediaAdapter mediaAdapter;
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file. Name: " + fileName);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media. " + audioType + " format not supported");
        }
    }
}
2. Facade
The Facade Pattern provides a simplified interface to a complex system of classes.
Use Case: Creating a simplified API for a complex system to handle different home theater systems (Amplifier, DVD player, Projector).
class HomeTheaterFacade {
    Amplifier amp;
    DvdPlayer dvd;
    Projector projector;

 public HomeTheaterFacade(Amplifier amp, DvdPlayer dvd, Projector projector) {
        this.amp = amp;
        this.dvd = dvd;
        this.projector = projector;
    }
    
 public void watchMovie(String movie) {
        amp.on();
        dvd.on();
        projector.on();
        System.out.println("Starting movie: " + movie);
    }
    public void endMovie() {
        amp.off();
        dvd.off();
        projector.off();
        System.out.println("Movie ended.");
    }
}
3. Proxy
The Proxy Pattern provides a surrogate or placeholder for another object to control access to it.
Use Case: Using a proxy to control access to an image and delay its loading until it’s actually needed.

interface Image {
    void display();
}
class RealImage implements Image {
    private String filename;
    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }
    private void loadFromDisk() {
        System.out.println("Loading " + filename);
    }
    public void display() {
        System.out.println("Displaying " + filename);
    }
}
class ProxyImage implements Image {
    private RealImage realImage;
    private String filename;
    public ProxyImage(String filename) {
        this.filename = filename;
    }
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
B. Creational Patterns
1. Singleton
The Singleton Pattern ensures a class has only one instance and provides a global point of access to it.
Use Case: Limiting database connections by using a single database instance.
public class Database {
    private static Database instance;
    private Database() {}
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}
2. Factory
The Factory Pattern provides an interface for creating objects but allows subclasses to alter the type of created objects.
Use Case: Creating different types of animals (Dog, Cat, etc.) in a zoo simulation.
abstract class Animal {
    public abstract void speak();
}
class Dog extends Animal {
    public void speak() {
        System.out.println("Woof");
    }
}
class Cat extends Animal {
    public void speak() {
        System.out.println("Meow");
    }
}
class AnimalFactory {
    public static Animal createAnimal(String type) {
        switch (type) {
            case "Dog": return new Dog();
            case "Cat": return new Cat();
            default: throw new IllegalArgumentException("Unknown animal type");
        }
    }
}
3. Builder
The Builder Pattern constructs complex objects by separating their creation from their representation.
Use Case: Building a detailed profile for a user, including optional attributes.
class User {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String address;

    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }
    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private String address;

        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }
        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }
        public User build() {
            return new User(this);
        }
    }
}
C. Behavioural Patterns
1. Chain of Responsibility
The Chain of Responsibility Pattern passes a request along a chain of handlers until one handles it.
Use Case: A logging system that passes messages along different loggers.
abstract class Logger {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;
    protected int level;
    protected Logger nextLogger;

    public void setNextLogger(Logger nextLogger) {
        this.nextLogger = nextLogger;
    }
    public void logMessage(int level, String message) {
        if (this.level <= level) {
            write(message);
        }
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }
    abstract protected void write(String message);
}
class ConsoleLogger extends Logger {
    public ConsoleLogger(int level) {
        this.level = level;
    }
    protected void write(String message) {
        System.out.println("Console::Logger: " + message);
    }
}
2. Memento
The Memento Pattern captures and externalizes an object’s internal state.
Use Case: Implementing an undo feature in an editor.
class Memento {
    private String state;
    public Memento(String state) { this.state = state; }
    public String getState() { return state; }
}
class Originator {
    private String state;
    public void setState(String state) { this.state = state; }
    public String getState() { return state; }
    public Memento saveStateToMemento() { return new Memento(state); }
    public void getStateFromMemento(Memento memento) { state = memento.getState(); }
}
3. Observer
The Observer Pattern allows an object to notify other objects of changes.
Use Case: Observing changes in a stock price.
interface Observer {
    void update(float price);
}
class Stock implements Observer {
    private String stockName;
    public Stock(String stockName) { this.stockName = stockName; }
    public void update(float price) {
        System.out.println(stockName + " updated price: " + price);
    }
}


