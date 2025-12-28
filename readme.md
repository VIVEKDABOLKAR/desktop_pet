it is first version very basic

it is simple pet application for desktop, it stay on top of your screen

# Clean and compile
mvn clean compile

# Run directly
mvn exec:java -Dexec.mainClass="com.pet.DesktopPetApp"

# Create executable JAR
mvn clean package

# Run the JAR
java -jar target/desktop-pet-1.0.0.jar