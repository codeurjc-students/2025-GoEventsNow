
> The following functionalities are classified into three levels according to their relevance, complexity and impact on the system. Each functionality is marked as implemented (✅) or in progress (⏳). The functionality classification helps to clearly define the development priorities and considerations:

### 🥉 **Basic**

> These functionalities represent the minimum required for the application to operate correctly and provide the event browsing and ticket management experience:

- ✅ **Anonymous User** 🕵️:
    - View and explore the main list of events.
    - View and explore the main list of participants.
    - View detailed information about specific events, including title, description, category, date, location, time, prices, available tickets, image and associated participants.
    - View detailed information about specific participants, including name, type, biography, image and events they are associated with.

- ✅ **Registered User** 👤:
    - Register, log in and log out securely.
    - View and edit their own profile information, including fullname, email and phone number.
    - Upload, update and delete their profile image.
    - View their purchased tickets from the user profile page and their details such as number of tickets, event details, price and ticket type.
    - Purchase event tickets, selecting the number of tickets and the ticket type (BASIC or VIP).

- ✅ **Administrator User** 👑:
    - Create, edit and delete events.
    - Create, edit and delete participants.
    - Upload, update and delete images for events and participants.

### 🥈 **Intermediate** 

> Functionalities that adds value and improves user experience:

- ⏳ **Advanced Search** 🔎: 
    - Filter events by category, date, participant and other event information.
    - Search through keyword search bar.
    - Combine multiple filters to refine results.
- ⏳ **Review System** ⭐:
    - **Registered User:** Add, modify, and delete their own reviews.
    - **Administrator User:** Manage and moderate user generated reviews.
- ✅ **Image Upload** 🖼️: 
    - Registered users can upload, update and delete their profile image.
    - Administrators can upload, update and delete images for events and participants.
    - Images are stored in the database.
- ⏳ **Statistics Charts** 📊:
    - Bar chart displaying the number of tickets sold per event.
    - Pie chart that categorizes sales based on event types.
- ⏳ **Help** ❓:
    - Implementation of a help center that includes a FAQ section with common questions and issues.
- ⏳ **Social Interaction** ❤️:
    - Functionality for registered users to save and manage a personalized list of favorite events.
    - Functionality for registered users to follow participants to receive upcoming events and other information.

### 🥇 **Advanced** 

> Functionality that allows obtaining the final version of the application, which are more complex, requiring algorithmic implementation, additional technologies or external service integration:

- ⏳ **Personalized Recommendation System** 🧠: 
    - Algorithm that generates personalized event feeds for each user, by analyzing user preferences based on:
        - Previously consumed genres or categories.
        - Event popularity metrics like number of tickets sold or rating.
- ⏳ **Digital Ticketing (PDF)** 📄:
    - Automatic generation of tickets in PDF format after successful purchase.   
- ⏳ **Email Service** 📧: 
    - Allow users to contact support through email.
    - Sends automated emails to users (purchase confirmations, reminders).
- ⏳ **Geolocation** 🗺️: 
    - Use of Google Maps/OpenStreetMap for event location display.
- ⏳ **Real-Time Notifications** 🔔: 
    - Implementation of WebSocket for live alerts (sold-out tickets, newly added events).
- ⏳ **Simulated Tickets Payment** 🔄: 
    - Implementation of a simulated payment gateway system to simulate a secure payment process.
- ⏳ **Sentiment Analysis (AI Rating)** 🤖: 
    - Automatic processing of user reviews in events by using NLP (Natural Language Processing) to classify comments. The process works as follows:
        - Each comment receives a sentiment label (Positive, Neutral or Negative), a sentiment score (0.00-1.00) and an AI rating based on the review.
        - The interface displays and compares both user rating and AI rating for error detection and to improve the recommendation system.

---