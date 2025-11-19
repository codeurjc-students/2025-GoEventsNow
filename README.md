# 🎟️ 2025-GoEventsNow

![GoEventsNow Logo](https://github.com/user-attachments/assets/1cbd97f1-e514-47d9-894d-658650db2900)

---
## 🧭 *Introduction*

> 🎟️ **GoEventsNow** is a modern and dynamic web platform designed to centralize the management and ticket sales for a wide variety of cultural, social and entertainment events, such as concerts, theatrical plays, talks, and more. The goal of this web is to offer users an unique experience to discover, explore and attend events they are interested in by purchasing tickets directly through the website.
>
> Users can explore events using advanced search and filtering options, view complete events and participants details, and register to access to to other features, such as purchasing tickets, managing their profiles, writing reviews, and more. In addition, GoEventsNow includes a personalized recommendation algorithm, real-time notifications and integrations with external services like PDF ticket generation and email ticket delivery.
>
> GoEventsNow aims to provide a complete, modern and user-centered system that simplifies the management and discovery of events, offering users a high quality experience. To achieve this, the project is developed using Spring Boot, Angular, and AI-based sentiment analysis, prioritizing technologies studied throughout the degree. The main goals are to ensure high code quality with solid development practices, guaranteeing scalability and maintainability in relation with the expectations of a Final Degree Project (TFG).
>
> This project is developed as part of my Final Degree Project (TFG) for the Degree in Software Engineering at the School of Computer Engineering (ETSII) of the King Juan Carlos University (URJC).
>
> *GoEventsNow: Your Next Event, Now, Simplified.* ✨ 

---
## 🖥️ *Screen Mockups*

> This section provides some key screens, which define the structure and the application functionalities. These screens illustrates the navigation flow that different kind of users can follow.
>
> ![Main Page](https://github.com/user-attachments/assets/2710564e-f00a-428f-b535-9f911e2aba1f)

---

> [!WARNING]
> At this stage, only the functional and technical objectives of the application have been defined. Implementation has not yet started, and the repository currently contains only the initial Phase 1 documentation.
> 
> 📁 *Repository: [GoEventsNow on GitHub](https://github.com/codeurjc-students/2025-GoEventsNow)*

---

## 🎯 *Objectives*

### 📈 **Functional Objectives**

> The goal of the application is to deliver a complete and creative web for managing and discovering events, making posible to users to register, explore, participate in and review a wide variety of events. Users will be able to manage their tickets, add events to their favorites list, follow participants and interact with additional features that make their experience in the web unique and enjoyable.

| Number | Functionality | Description |
| :-: | :--- | :--- |
| **1** | **Discovery & Search** 🔍 | Users can view, search and filter events using multiple criteria such as category, date and location. |
| **2** | **User Activity** 🔐 | Provides a secure system for user registration, login, and profile management (including purchase history and profile editing). |
| **3** | **Ticketing & History** 🎟️ | Registered users can securely purchase tickets through the payment gateway and access to their historical record of all their transactions. |
| **4** | **Events rating** ⭐ | Registered users can rate and review the events they attend, providing feedback for other users. |
| **5** | **Content Control** ⚙️ | Administrators can add, delete and modify events or participants. |
| **6** | **Algorithm Personalization** 🧠 | Implements a recommendation system based on user history and event popularity to personalize event discovery. |
| **7** | **Digital Ticketing** 📄📧 | Tickets can be downloaded in PDF format and sent via email once the purchase is successfully completed. |
| **8** | **Chart Reports** 📊 | Users can visualize events statistics based on sales performance and attendance data for each event (Bar and Pie charts). |
| **9** | **Real Time Communication** 🔔 | Users will receive real-time notifications (tickets sold out, purchase alerts). |
| **10** | **AI Sentiment Analysis** 🤖 | Users reviews will be analized using an AI sentiment analysis system, generating rating suggestions and classifying comments to improve recommendations. |

### 💻 **Technical Objectives**

> The project will be a full-stack application, using the modern technologies studied during the degree, such as Spring Boot, Angular or MySQL. The goal is to guarantee high code quality through the application of good practices, ensuring scalability, maintainability, and delivering a unique user experience, aligning the implementation with the highest standards expected of an Engineering graduate.

| Number | Technical Aspect | Description |
| :-: | :--- | :--- |
| **1** | **Modern Technologies** 🌐 | Implementation using the recommended stack: Backend API REST using Spring Boot and Frontend SPA using Angular. |
| **2** | **Continuous Integration and Deployment (CI/CD)** 🚀 | Implementation of the CI/CD pipeline using GitHub Actions, ensuring automated quality control and continuous delivery. |
| **3** | **Automated Testing** 🧪 | Application of automated testing (Unit, Integration, E2E) using technologies like Java, JUnit, Selenium, and Rest Assured. |
| **4** | **Packaging & Deployment** ☁️ | Containerization of the entire application using Docker for consistent local development and deployment in Cloud platform (AWS). |
| **5** | **Data Persistence** 🗄️ | Use of MySQL as the relational database for application persistence and data management. |
| **6** | **Repository** 💾 | Use of GitHub for version control and GitHub Projects for project tracking and transparency |
| **7** | **External Service Integration** 🛰️ | Integrate complementary technologies like PDF generation and geolocation (Google Maps/OpenStreetMap). |
| **8** | **AI Integration (NLP)** 🤖 | Integration of a NLP microservice, implemented with open-source Transformer models, responsible for multilingual sentiment analysis of user reviews. |
| **9** | **Code Quality Analysis** 🔍 | Integration of SonarQube for automated static code analysis, resolving possible code violations. |
| **10** | **Real-Time Communication** 🔔 | Implementation of WebSockets for delivering real-time notifications such as tickets and events updates. |

---

## 🛠️ *Methodology*

> The application development will follow an iterative and incremental methodology, structured into seven main phases and managed through GitHub Projects for continuous monitoring, planning and task management, ensuring transparency regarding task status. The continious monitoring and strict emphasis on quality control aligns with the principles of agility, delivering value in increments. Progress will be monitored through periodic meetings and email communication with the tutor, who acts as the Project Manager. Version control will be stick strictly to the GitHub repository, where the functionalities are made in differents `release` branches, remaining the `main` branch stable all time, and finally integrating the changes only after a mandatory Pull Request process.

### 🧩 **Phased Development Cycle**

| Phase | Target | Start Date | End Date |
| :--- | :--- | :--- | :--- |
| **Phase 1: Functionalities definition** 📝 |Complete `README.md` with the website documentation (Objectives, Methodology, Basic, Intermediate and Advanced functionalities, Analysis and Screen Mockups). | [26/10/2025] | [../../....] |
| **Phase 2: Configuration & CI** 🧪 | Set repository structure, CI/CD pipeline, automated testing setup, and minimum functionality implemented. | [../../....] | [../../....] |
| **Phase 3: Basic Functionality & Docker** 🥉 | Implementation of Basic Functionalities. Release v0.1 published with Docker. | [../../....] | [../../....] |
| **Phase 4: Intermediate Functionality** 🥈 | Implementation of Intermediate Functionalities. Release v0.2 published and application deployed. | [../../....] | [../../....] |
| **Phase 5: Advanced Functionality** 🥇 | Implementation of Advanced Functionalities. Release v1.0 published and project finalized. | [../../....] | [../../....] |
| **Phase 6: Report Writing** ✍️ | Final Degree Project (TFG) report writing. | [../../....] | [../../....] |
| **Phase 7: Defense** 🎤 | Preparation and execution of the TFG defense. | [../../....] | [../../....] |

### 📅 **Project Schedule (Gantt Diagram)**

> The following Gantt diagram visually represents the temporal planning of the seven development phases, establishing the start and end dates for each project phase.
>
> ![ganttDiagram](https://github.com/user-attachments/assets/af41d9e2-7475-455c-939d-425a66202f6f)

---

## ⚙️ *Detailed Functionalities*

> The following functionalities are classified into three levels according to their relevance, complexity and impact on the system. This classification helps to clearly define the development priorities and considerations:

### 🥉 **Basic**

> These functionalities represents the minimum required for the application to operate correctly and provide the event browsing and ticket management experience:

- **Anonymous User** 🕵️:
    - View and explore the main list of events.
    - View and explore the main list of participants.
    - View detailed information about specific events (description, category, date, location, participants, image, reviews) and participants (description, category, image).
- **Registered User** 👤:
    - Secure user registration and authentication.
    - Proile management: personal details, image, email, purchase history, followed participants, favorites events.
    - Purchase event tickets.
    - Cancel previous purchased tickets.
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
    - Pie chart that categorize sales based on event types.
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
    - Implementation of WebSockets for live alerts (sold-out tickets, newly added events).
- **Simulated Tickets Payment** 🔄: 
    - Implementation of a simulated payment gateway system to simulate a secure payment process .
- **Sentiment Analysis (AI Rating)** 🤖: 
    - Automatic processing of user reviews in events by using NLP (Natural Language Processing) to classify coments. The process works as follows:
        - Each comment receives a sentiment label (Positive, Neutral or Negative), a sentiment score (0.00-1.00) and a rating AI based on the review.
        - The interface displays and compares both user rating and AI rating for error detection and to improve the recommendation system.

---

## 🔍 *Analysis*

### 📱 **Screens & Navigation**

> The design of the application's screens and navigation has been carried out using the design tool Figma. This decision was not only because it provides a efficient and clear environment for creating wireframes, but also because I already had previous experience using it during the degree in other projects. Each wireframe is defined along with a description of its functionality and the elements it contains.
> 
> The goal of these wireframes is to provide a conceptual representation of the user interface without focusing on visual styling or implementation details. They define the structure, layout, and navigation flow of the main pages of the system, specifically covering the Basic and Intermediate functionalities defined. Advanced functionalities are intentionally excluded from the visual design, since they require additional technologies or depend on external services.

#### 1. Home / Main Screen (Event List)

| Description |
| :--- |
| Main page screen that allows any user to view the main list of available events. It includes basic information about the different kind of events (title, date, location, price, category) and a simplified navigation header for authentication and other sections.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Event details** | Event Detail Screen | By clicking "View details" button in the event card. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="mainScreen" src="https://github.com/user-attachments/assets/2710564e-f00a-428f-b535-9f911e2aba1f" />
</p>

#### 2. Event Detail Screen 

| Description |
| :--- |
| This screen displays to all users complete and specific information about a selected event, including description, rating, participants, location, reviews, date and ticket availability. It also includes features to registered users like the possibility to add the event to favorites, to get tickets to assist or to write a review.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Purchase tickets page** | Purchase Tickets Screen | By clicking "Get Tickets" button in the card. Only to registered users. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="eventDetail" src="https://github.com/user-attachments/assets/ebe98fb7-a053-4a51-9333-87fddcd16318" />
<img width="450" height="450" alt="reviewImage" src="https://github.com/user-attachments/assets/0946ea83-d997-44d3-98f9-c18f26288760" />
</p>

#### 3. Participant List Screen 

| Description |
| :--- |
| This screen allows all users to view and discover the registered participants and basic information of each of them (name, description, followers and category). It includes a search bar, category filters to refine the list and the possibility to view the complete profile.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participant details** | Participant Detail Screen | By clicking "View profile" button in the participant card. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="participantList" src="https://github.com/user-attachments/assets/91c67757-270f-49b8-8e99-a835970eb882" />
</p>

#### 4. Participant Detail Screen 

| Description |
| :--- |
| This screen provides detailed information about a specific participant including their name, category, biography, followers and events participate in. It also includes features to registered users to follow the participant, to easily be informed about upcoming events.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Event detail** | Event Detail Screen | By clicking "View Event" button in the event card in "Upcoming Events" |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="participantDetail" src="https://github.com/user-attachments/assets/13cb1e35-c0cd-41bc-9862-923514807119" />
</p>

#### 5. Search Event Screen 

| Description |
| :--- |
| This screen allows all users to discover events and display specifics resulting events depending of different kinds of filters, such as category, date or price.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Event detail** | Event Detail Screen | By clicking "View Event" button in the event card in "Upcoming Events" |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="searchEvent" src="https://github.com/user-attachments/assets/1eee1d72-e94c-4c77-a741-210e669cf4db" />
</p>

#### 6. Log In Screen 

| Description |
| :--- |
| Screen for user authentication, allowing users to sign in to their existing account using their email/username and password. It includes "Sign up" link in order to direct new users to the registration page.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |

---

<p align="center">
<img width="800" alt="logIn" src="https://github.com/user-attachments/assets/7ae6c375-987e-4dfc-82a7-16aef5b9dcc2" />
</p>

#### 7. Sign Up Screen 

| Description |
| :--- |
| Screen that allows new users to create an account to join GoEventsNow. The form requires essential credentials (username, email and password) to ensure security and to transform anonymous users to gain the "Registered user" permissions.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |

---

<p align="center">
<img width="800" alt="signUp" src="https://github.com/user-attachments/assets/0cf1edc8-07a9-4fe9-9a0e-2e4c8a713030" />
</p>

#### 8. User Profile Screen 

| Description |
| :--- |
| This screen provides personal information (username, tickets bought, email address or fullname) for authenticated users and the possibility to edit some personal information. It includes features to see the specific tickets bought, favorites events and participants following.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. |

---

<p align="center">
<img width="800" alt="userProfile" src="https://github.com/user-attachments/assets/93a4f695-a2d2-483d-bde2-3d473cdae9f9" />
<img width="500" height="500" alt="myTickets" src="https://github.com/user-attachments/assets/37707d68-4389-4f07-aed8-c2ffa9db029e" />
</p>

#### 9. Dashboard Chart Screen 

| Description |
| :--- |
| This screen allows the administrator to see statistics and analytics by displaying summarized metrics (total events, total tickets sold, total revenue or active users) and detailed charts (Bar chart for sales per event and Pie chart for sales for category).|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign out** | Log In / Home Screen | By clicking the exit icon in the header. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. |

---

<p align="center">
<img width="800" alt="chartScreen" src="https://github.com/user-attachments/assets/46f29b3f-d499-4fac-9120-bb3d831a85e2" />
</p>

#### 10. Manage Events Screen

| Description |
| :--- |
| This screen is an event management only accesible to the administrator. It displays a list of current events, with summary details and icon to modify or delete, and a specific button in order to add new events, with the new event specific information.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign out** | Log In / Home Screen | By clicking the exit icon in the header. |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. |

---

<p align="center">
<img width="800" alt="manageEvent" src="https://github.com/user-attachments/assets/30232372-d7e5-4e8c-b84e-88bf7f2f1664" />
<img width="400" height="400" alt="addEvent" src="https://github.com/user-attachments/assets/76a034e4-b64b-4dba-840c-d805c6edbc7b" />
</p>

#### 11. Manage Participants Screen

| Description |
| :--- |
| This screen is a participant management only accesible to the administrator. It displays a list of current participants, with summary details and icon to modify or delete, and a specific button in order to add new participants, with the new participant specific information.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign out** | Log In / Home Screen | By clicking the exit icon in the header. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. |

---

<p align="center">
<img width="800" alt="manageParticipant" src="https://github.com/user-attachments/assets/68344ece-a31b-4a85-b706-9c30425362d2" />
</p>

#### 12. Purchase Tickets Screen

| Description |
| :--- |
| This screen manages the final step in order to complete the purchase functionality. It allows registered user to select the number of tickets to buy and confirm the payment.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Help page** | Help / FAQ Screen | By clicking "Help" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. |

---

<p align="center">
<img width="800" alt="purchaseTicket" src="https://github.com/user-attachments/assets/5e99ee53-06f4-4714-8402-887db7981324" />
</p>

#### 13. Help / FAQ Screen 

| Description |
| :--- |
| The help page allows users to find quick answers to common questions about issues or doubts in the application.|

##### ➡️ Navigation Links
| Navigation | Destination | Method |
| :--- | :--- | :--- |
| **Events list** | Home / Main Screen | By clicking "Events" button in the header. |
| **Participants list** | Participant List Screen | By clicking "Participants" button in the header. |
| **Sign up** | Sign Up Screen | By clicking "Sign up" button in the header. (Anonymous user) |
| **Search events page** | Search Event Screen | By clicking "Search" button in the header. |
| **Log in** | Log In Screen | By clicking "Log in" button in the header. (Anonymous user) |
| **Manage Events** | Manage Events Screen | By clicking "Manage Events" button in the header. (Administrator user) |
| **Manage Participants** | Manage Participants Screen | By clicking "Manage Participants" button in the header. (Administrator user) |
| **Dashboard** | Graphic Dashboard Screen | By clicking "Dashboard" button in the header. (Administrator user) |
| **Profile** | User Profile Screen | By clicking "Profile" button in the header. (Registered user) |
| **Sign out** | Home / Main Screen | By clicking the exit icon in the header. (Registered user) |

---

<p align="center">
<img width="800" alt="FAQ" src="https://github.com/user-attachments/assets/2b4b8060-75a0-49ca-b180-9d76565a1a65" />
</p>

---

### 🧱 **Entities**

> The application manages five main entities, each with defined attributes and relationships:

| Entity | Primary Attributes | Relationships |
| :--- | :--- | :--- |
| **User** | `Username`, `Full Name`, `Password`, `Email`, `Image` (Profile photo), `Role` (Registered/Admin) | Can write zero or many reviews (0,N). Can obtain zero or many tickets (0,N). Can follow zero or many participants (0,N). Can save zero or many events (0,N).|
| **Event** | `Title`, `Description`, `Category`, `Date`, `Image` (Promotional poster), `Location`, `Maximum Capacity` | Can have zero or many tickets sold (0,N). Can have zero or many user reviews (0,N). Has a minimum of 1 participant (1,N). Can be saved by zero or many users (0,N)|
| **Participant**| `Name`, `Image`, `Type`, `Biography` | Can participate in zero or many events (0,N). Can be followed by zero or many users (0,N) |
| **Ticket** | `Price`, `Ticket Type`, `Quantity` | Only 1 user associated (1,1). Only 1 event associated (1,1). |
| **Review** | `Comment`, `Rating`, `AI Rating` | Only 1 user associated (1,1). Only 1 event associated (1,1). |

### 🔐 **User Permissions**

> Permissions based on user roles:

| User Type | View/Search | Buy Tickets | Post/Edit Reviews | Manage Events, Participants, Reviews |
| :--- | :--- | :--- | :--- | :--- |
| **Anonymous** | **✔️** (Events, Reviews, Participants) | ❌ | ❌ | ❌ |
| **Registered User** | ✔️ | ✔️ | **✔️** (Only their own reviews) | ❌ |
| **Administrator** | ✔️ | ✔️ | **✔️** (All Reviews) | ✔️ |

### 🖼️ **Images**

> The following entities include one or more images associated with them:

| Entity | Image Description |
| :--- | :--- |
| **Registered User & Administrator** | Profile photo |
| **Events** | Promotional event poster |
| **Participants** | Profile photo |

### 📊 **Charts**

> Statistical data will be shown using the following visual charts:

- **Bar Chart:** Show the attendance per event.
- **Pie Chart:** Show the percentage of tickets depending on the event type.

### 🧰 **Complementary Technologies**

> The project will integrate the following external technologies:

- **PDF Generation:** To generate the tickets bought.
- **Geolocation:** Use of Google Maps/OpenStreetMap to display the exact event location.
- **Email Service:** To send emails with information such as the purchase confirmations or reminders.
- **Real-Time communication**: To deliver real-time notifications with WebSockets like ticket and event updates.
- **AI Sentiment Analysis**: Python microservice that processes user reviews to classify sentiment and improve event recommendations.
- **Simulated Payment Gateway**: Python microservice that emulates a payment gateway to securely simulate ticket purchases.

### 🧠 **Advanced Algorithms**

> The advanced algorithm implementation is the **Event Recommendation System**. This system will sophisticated data processing and filtering techniques to personalize the user's experience by ordering and prioritizing events on the main interface based on two key metrics:

- **Personalized Affinity:** Prioritizing events based on the user's purchase history and identified favorite genres.
- **Market Popularity:** Sorting and ranking results by popularity, defined by the total number of tickets sold and current trending activity.

---

## 📈 *Tracking*

> Project tracking and task progress will be managed using the following tools:

- 📝 **Blog Medium:** Planned for updates and development summaries during the project -> *[Blog Medium](https://medium.com/@albertoml1799)*.
- 💻 **GitHub Project:** Used to organize and manage the project tasks, set their status and time spent -> *[GoEventsNow Project Board](https://github.com/orgs/codeurjc-students/projects/37)*.

---


## 👨‍💻 *Author*

> The development of this application is carried out within the context of the Final Degree Project (TFG) for the Degree in Software Engineering at the School of Computer Engineering (ETSII) of the URJC.

---

| **Role** | **Full Name** | **Email** | **GitHub** |
| :--- | :--- | :--- | :--- |
| 🎓 **Student** | Alberto Manjón López | a.manjon.2022@alumnos.urjc.es | [AlbertoML1999](https://github.com/AlbertoML1999) |
| 🎯 **Tutor** | Óscar Soto Sánchez | oscar.soto@urjc.es | [OscarSotoSanchez](https://github.com/OscarSotoSanchez) |

---
