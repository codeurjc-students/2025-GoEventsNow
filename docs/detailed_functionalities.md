
> The following functionalities are classified into three levels according to their relevance, complexity and impact on the system. This classification helps to clearly define the development priorities and considerations:

### 🥉 **Basic**

> These functionalities represent the minimum required for the application to operate correctly and provide the event browsing and ticket management experience:

- **Anonymous User** 🕵️:
    - View and explore the main list of events.
    - View and explore the main list of participants.
    - View detailed information about specific events (description, category, date, location, participants, image, reviews) and participants (description, category, image).
- **Registered User** 👤:
    - Secure user registration and authentication.
    - Explore and edit profile: personal details, image, email, purchase history, followed participants, favorites events.
    - Purchase event tickets.
    - Cancel previously purchased tickets.
- **Administrator User** 👑:
    - Add, delete, and modify operations on Events.
    - Add, delete, and modify operations on Participants.

### 🥈 **Intermediate** 

> Functionalities that adds value and improves user experience:

- **Advanced Search** 🔎: 
    - Filter events by category, date, participant and other event information.
    - Search through keyword search bar.
    - Combine multiple filters to refine results.
- **Review System** ⭐:
    - **Registered User:** Add, modify, and delete their own reviews.
    - **Administrator User:** Manage and moderate user generated reviews.
- **Image Upload** 🖼️: 
    - Upload and update profile images for registered users and participants.
    - Upload event promotional posters and images.
- **Statistics Charts** 📊:
    - Bar chart displaying the number of tickets sold per event.
    - Pie chart that categorizes sales based on event types.
- **Help** ❓:
    - Implementation of a help center that includes a FAQ section with common questions and issues.
- **Social Interaction** ❤️:
    - Functionality for registered users to save and manage a personalized list of favorite events.
    - Functionality for registered users to follow participants to receive upcoming events and other information.

### 🥇 **Advanced** 

> Functionality that allows obtaining the final version of the application, which are more complex, requiring algorithmic implementation, additional technologies or external service integration:

- **Personalized Recommendation System** 🧠: 
    - Algorithm that generates personalized event feeds for each user, by analyzing user preferences based on:
        - Previously consumed genres or categories.
        - Event popularity metrics like number of tickets sold or rating.
- **Digital Ticketing (PDF)** 📄:
    - Automatic generation of tickets in PDF format after successful purchase.   
- **Email Service** 📧: 
    - Allow users to contact support through email.
    - Sends automated emails to users (purchase confirmations, reminders).
- **Geolocation** 🗺️: 
    - Use of Google Maps/OpenStreetMap for event location display.
- **Real-Time Notifications** 🔔: 
    - Implementation of WebSocket for live alerts (sold-out tickets, newly added events).
- **Simulated Tickets Payment** 🔄: 
    - Implementation of a simulated payment gateway system to simulate a secure payment process.
- **Sentiment Analysis (AI Rating)** 🤖: 
    - Automatic processing of user reviews in events by using NLP (Natural Language Processing) to classify comments. The process works as follows:
        - Each comment receives a sentiment label (Positive, Neutral or Negative), a sentiment score (0.00-1.00) and an AI rating based on the review.
        - The interface displays and compares both user rating and AI rating for error detection and to improve the recommendation system.

---