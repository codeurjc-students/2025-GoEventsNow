
### 📱 **Screens & Navigation**

> The design of the application screens and navigation has been carried out using the design tool Figma. This decision was not only because it provides an efficient and clear environment for creating wireframes, but also because I already had previous experience using it during the degree in other projects. Each wireframe is defined along with a description of its functionality.
> 
> The goal of these wireframes is to provide a conceptual representation of the user interface without focusing on visual styling or implementation details. They define the structure, layout, and navigation flow of the main pages of the system, specifically covering the Basic and Intermediate functionalities defined. Advanced functionalities are intentionally excluded from the visual design, since they require additional technologies or depend on external services.

#### 1. Home / Main Screen (Event List)

| Description |
| :--- |
| Main page screen that allows any user to view the main list of available events. It includes basic information about the different kind of events (title, date, location, price, category) and a simplified navigation header for authentication and other sections.|

---
![Main Screen](https://github.com/user-attachments/assets/2710564e-f00a-428f-b535-9f911e2aba1f)

#### 2. Event Detail Screen 

| Description |
| :--- |
| This screen displays to all users complete and specific information about a selected event, including description, rating, participants, location, reviews, date and ticket availability. It also includes features to registered users like the possibility to add the event to favorites, to get tickets or to write a review.|

---
![Event Detail](https://github.com/user-attachments/assets/ebe98fb7-a053-4a51-9333-87fddcd16318)

#### 3. Participant List Screen 

| Description |
| :--- |
| This screen allows all users to view and discover the registered participants and basic information of each of them (name, description, followers and category). It includes a search bar, category filters to refine the list and the possibility to view the complete profile.|

---
![Participant List](https://github.com/user-attachments/assets/91c67757-270f-49b8-8e99-a835970eb882)

#### 4. Participant Detail Screen 

| Description |
| :--- |
| This screen provides detailed information about a specific participant including their name, category, biography, followers and events they participated in. It also includes features to registered users to follow the participant, to easily be informed about upcoming events.|

---
![Participant Detail](https://github.com/user-attachments/assets/13cb1e35-c0cd-41bc-9862-923514807119)

#### 5. Search Event Screen 

| Description |
| :--- |
| This screen allows all users to discover events and display specific resulting events depending on different kinds of filters, such as category, date or price.|

---
![Search Event](https://github.com/user-attachments/assets/1eee1d72-e94c-4c77-a741-210e669cf4db)

#### 6. Log In Screen 

| Description |
| :--- |
| Screen for user authentication, allowing users to log in to their existing account using their email/username and password. It includes "Sign up" link in order to redirect new users to the registration page.|

---
![Log In](https://github.com/user-attachments/assets/7ae6c375-987e-4dfc-82a7-16aef5b9dcc2)

#### 7. Sign Up Screen 

| Description |
| :--- |
| Screen that allows new users to create an account to join GoEventsNow. The form requires essential credentials (username, email and password) to ensure security and to transform anonymous users into registered users.|

---
![Sign Up](https://github.com/user-attachments/assets/0cf1edc8-07a9-4fe9-9a0e-2e4c8a713030)

#### 8. User Profile Screen 

| Description |
| :--- |
| This screen provides personal information (username, tickets bought, email address or full name) for authenticated users and the possibility to edit some personal information. It includes features to see the specific tickets bought, favorites events and participants following.|

---
![User Profile](https://github.com/user-attachments/assets/93a4f695-a2d2-483d-bde2-3d473cdae9f9)

#### 9. Chart Screen 

| Description |
| :--- |
| This screen allows users to see statistics and analytics by displaying summarized metrics and detailed charts (Bar chart for sales per event and Pie chart for sales for category).|

---
![Chart Screen](https://github.com/user-attachments/assets/46f29b3f-d499-4fac-9120-bb3d831a85e2)

#### 10. Manage Events Screen

| Description |
| :--- |
| This screen is an event management only accessible to the administrator. It displays a list of current events, with summary details and icon to modify or delete, and a specific button in order to add new events.|

---
![Manage Events](https://github.com/user-attachments/assets/30232372-d7e5-4e8c-b84e-88bf7f2f1664)

#### 11. Manage Participants Screen

| Description |
| :--- |
| This screen is a participant management only accessible to the administrator. It displays a list of current participants, with summary details and icon to modify or delete, and a specific button in order to add new participants.|

---
![Manage Participants](https://github.com/user-attachments/assets/68344ece-a31b-4a85-b706-9c30425362d2)

#### 12. Purchase Tickets Screen

| Description |
| :--- |
| This screen manages the final step required to complete the purchase process. It allows registered user to select the number of tickets to buy and confirm the payment.|

---
![Purchase Ticket](https://github.com/user-attachments/assets/5e99ee53-06f4-4714-8402-887db7981324)

#### 13. Help / FAQ Screen 

| Description |
| :--- |
| The help page allows users to find quick answers to common questions about issues or doubts in the application.|

---
![FAQ](https://github.com/user-attachments/assets/2b4b8060-75a0-49ca-b180-9d76565a1a65)

---
#### 🗺️ Navigation Diagram

> [!NOTE]
> The header routes are not considered, except in the Main Screen.
> Colors in arrows represent role permissions:
> - 🟡 Anonymous, Registered, Admin
> - 🟢 Registered, Admin
> - 🔵 Admin only

---
![Navigation Diagram](https://github.com/user-attachments/assets/807f68e0-78e7-40bc-b699-2ea43d2c6886)


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

| User Type | View/Search | Buy Tickets | Post/Delete Reviews | Manage Events, Participants, Reviews |
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
