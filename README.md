# Irish Road Shares

## Table of Contents

- [Introduction](#introduction)
- [Screenshots](#screenshots)
- [Entity Diagram](#entity-diagram)
- [Features](#features)
- [Navigation](#navigation)
- [Footer](#footer)
- [Technologies & Frameworks](#technologies--frameworks)
- [Installation & Deployment](#installation--deployment)
- [Viewing the App](#viewing-the-app)
- [Author](#author)
- [References](#references)

## Introduction

Irish Road Shares is a meticulously crafted Android application that revolutionizes the carpooling experience in Ireland. By seamlessly integrating real-time chat functionality, users can effortlessly communicate, ensuring that coordinating trips is both efficient and enjoyable. The app aims to cater to a wide audience, from daily commuters to occasional travelers, providing a platform that is both intuitive and comprehensive.

## Screenshots

### Login Screen
![WhatsApp Image 2023-06-19 at 03 44 19](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/94041e42-bb17-46ca-8601-1ae55333a274)

![WhatsApp Image 2023-09-13 at 02 43 17](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/00633cc0-9ec5-40ab-8620-e0c9b83fa128)

**Description:** The initial gateway to the Irish Road Shares experience. Designed with user-centric principles, this screen offers a clear and concise interface for users to input their credentials and gain access to the platform. The emphasis on simplicity ensures that users, regardless of their tech-savviness, can easily log in without any hiccups.

### Signup Screen
![WhatsApp Image 2023-09-13 at 02 46 51](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/9b3547ce-63f2-40d5-b179-4b33772393f0)

**Description:** For new users, the Signup screen is their first step into the world of Irish Road Shares. It's designed to be straightforward, requesting essential details such as username, email, phone number, and password. The intuitive layout ensures a hassle-free registration process, paving the way for a delightful user journey ahead.

### Main Dashboard
![WhatsApp Image 2023-06-19 at 03 45 42](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/9f7f0979-601e-490d-bfea-0015be58647d)

**Description:** Serving as the central hub of the application, the Main Dashboard welcomes users with a dynamic visual of a moving roadway, symbolizing the journey they're about to embark on. Personalized with a greeting, "Hello, [Username]", it provides three primary actions represented by visually appealing icons: Search Trips, Post a Trip, and Chats. Each icon is self-explanatory, ensuring users can navigate with ease.

### Search Trips
![WhatsApp Image 2023-06-19 at 03 53 28](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/886432bd-d0ba-4790-9e87-378ebf30ff0f)
![WhatsApp Image 2023-09-13 at 02 51 21](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/47ffbcd7-6e4d-41f9-aa4e-bb8b94e62fe6)

**Description:** A core feature of the app, the Search Trips screen is where users can find trips that align with their preferences. With fields to input departure and destination points and buttons for selecting dates, it offers a dynamic search experience. Once the search criteria are set, a list of matching trips is displayed, each detailed enough for users to make informed decisions.

### Chat Interface
![WhatsApp Image 2023-06-19 at 03 45 42 (1)](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/21840e61-89a7-4abb-aa28-186ff861843a)

**Description:** Communication is key in any carpooling app, and the Chat Interface of Irish Road Shares is no exception. Powered by real-time messaging capabilities, this interface allows users to communicate seamlessly with trip participants or trip posters. For trip posters, an added feature is the ability to accept or decline passenger requests, making the coordination process efficient.

### Post a Trip
![Post a Trip](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/c51a9944-1c20-49b7-9863-8f8a115e4f32)

**Description:** For users looking to offer rides, the Post a Trip screen is their canvas. It provides a structured interface to input all necessary trip details, from departure and destination points to car details and available seats. The process is designed to be intuitive, ensuring that users can post their trips with ease.


### Footer
![WhatsApp Image 2023-09-13 at 03 15 09](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/ca225599-0909-4b75-9151-0b4c3b5f4b43)

**Description:** A minimalist yet functional footer is present across various screens of the app. It offers quick access to the three core functionalities: Post a Trip, Search a Trip, and Messages. This design choice ensures that users can swiftly navigate the app without constantly returning to the main dashboard.


### Video of the app running 
https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/e18b9547-f372-4413-bb29-d6ce38df3eca






## Entity Diagram

Irish Road Shares is built upon a comprehensive and structured data model, ensuring seamless interactions, data integrity, and efficient data retrieval and storage:


![entitydiagram](https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada/assets/115966506/96d577f4-5e65-4706-8425-11a0a75f6a79)


### User:
- `userId`: A unique identifier for each user, ensuring individual data separation and easy referencing.
- `name`: Captures the full name of the user, facilitating personalized interactions and greetings within the app.
- `phone`: A vital field for potential contact, emergency situations, or verification purposes.
- `email`: Primarily used for communication, sending notifications, and serving as a unique login credential.
- `password`: Stored securely using state-of-the-art encryption techniques to ensure user data protection and prevent unauthorized access.

### Trips:
- `tripId`: A unique identifier for each trip, facilitating easy referencing and management.
- `additionalComments`: Space for any extra information or special instructions the trip creator wants to convey to potential passengers.
- `carMake` & `carModel`: Provides details about the vehicle being used for the trip, giving passengers an idea of the ride they'll be on.
- `departure` & `destination`: Clearly defined start and end points for the trip, with associated latitude and longitude details for accurate location tracking and mapping.

### TripRequests:
- `requestId`: A unique identifier for each trip request, ensuring each request is individually tracked and managed.
- `seatsToBook`: Specifies the number of seats a user wishes to book, allowing the trip poster to manage available space.
- `status`: Reflects the current state of the request, indicating whether it's pending, accepted, or declined.
- `tripId`: Establishes a link to a specific trip, ensuring the request is associated with the correct journey.

### Conversation & Messages:
- `conversationId`: A unique identifier for each conversation, ensuring messages are grouped correctly.
- `tripId`: Links the conversation to a specific trip, providing context to the discussion.
- `messages`: A list or collection of individual messages, each having:
  - `content`: The actual text of the message.
  - `senderId`: Identifier for the user who sent the message, ensuring accountability and clarity in conversations.
  - `senderName`: The name of the user who sent the message, providing a more human-readable context.
  - `timestamp`: The exact time the message was sent, ensuring conversations are chronologically ordered and easy to follow.



## Features

- **User Authentication**: 
  - **Security**: Utilizing Firebase's robust backend, the app ensures a secure registration and login process. Passwords are encrypted, and multi-factor authentication can be enabled for added security.
  - **User Experience**: The authentication process is designed to be swift and user-friendly, with clear prompts and feedback to guide users.
  - **Data Protection**: Ensures that user data remains confidential, preventing unauthorized access and potential breaches.

- **Trip Management**: 
  - **Creation**: Users can effortlessly create trips, inputting all necessary details from vehicle type to departure times.
  - **Viewing**: A comprehensive list or grid view displays available trips, with options to sort or filter results.
  - **Joining**: With a simple click, users can request to join a trip, with the trip owner receiving a request for approval.
  - **Details**: Each trip provides exhaustive information, from the vehicle used to the departure and destination points, ensuring users are well-informed.

- **Real-time Chat**: 
  - **Instant Messaging**: Leveraging Firestore's capabilities, the chat feature allows for real-time communication between users.
  - **Contextual Chats**: Conversations are linked to specific trips or requests, ensuring discussions are contextually relevant.

- **Search & Filter**: 
  - **Dynamic Search**: Users can input specific criteria, from locations to dates, to find trips that match their needs.
  - **Advanced Filters**: Beyond basic search, users can refine results based on preferences like departure time, vehicle type, and more. This ensures that users find the perfect trip that aligns with their requirements.
  - **User-friendly Interface**: The search and filter UI is intuitive, with clear labels, prompts, and feedback.

- **Trip Requests Management**:
  - **Request Status**: Users can track the status of their trip requests, whether they're pending, accepted, or declined.
  - **Seamless Integration**: Trip requests are seamlessly integrated into the chat, allowing for easy communication between the trip owner and the requester.

## Navigation

Irish Road Shares offers a fluid and intuitive navigation experience. Starting with a captivating splash screen that embodies the app's essence, users are then directed to either log in or sign up. The authentication process is streamlined, ensuring users can quickly dive into the app's main functionalities.

Once inside, the main dashboard serves as the central hub, presenting users with clear options: searching for trips, posting a new trip, or diving into their chats. Each section is designed with user experience in mind, ensuring that even first-time users can navigate with ease. The footer provides quick access to these core functionalities, reducing the need to return to the main dashboard continually.

The search trips section offers a dynamic interface, allowing users to input their preferences and find the best matches. On the other hand, the post a trip section guides users through the process, ensuring all necessary details are captured. The chat interface is clean and user-friendly, promoting seamless communication between users.

## Footer

A minimalist footer, ensuring users can swiftly navigate between the app's primary functionalities without the need to return to the main dashboard.

## Technologies & Frameworks

- **Android Studio**: 
  - **Integrated Development Environment (IDE)**: The primary tool for Android application development, offering a comprehensive suite of tools for designing, coding, and testing.
  - **Emulator**: Allows developers to test the application in various Android versions and screen sizes without needing physical devices.
  - **Profiler**: Provides real-time statistics of an app's CPU, memory, and network activity, aiding in performance optimization.

- **Firebase**: 
  - **Real-time Database**: Enables data to be stored and synchronized in real-time, ensuring all users have the most up-to-date information.
  - **Authentication**: Provides services and tools to authenticate users using email, phone numbers, and even third-party providers like Google and Facebook.
  - **Firestore**: A flexible, scalable database for mobile, web, and server development. It keeps data in sync across client apps through real-time listeners.
  - **Cloud Functions**: Allows the execution of server-side code in response to events triggered by Firebase features and HTTPS requests.

- **Java**: 
  - **Object-Oriented Programming**: The primary language used for Android app development, Java allows for modular and organized code.
  - **Libraries**: Numerous libraries are available to extend Java's capabilities, from networking to data processing.

- **XML**:
  - **User Interface Design**: XML is used in Android development to design and layout screens, ensuring a consistent and responsive user interface.
  - **Custom Themes and Styles**: Allows for the creation of custom app themes, ensuring a consistent look and feel across the application.

- **Gradle**:
  - **Build Automation**: Automates the building process of the app, ensuring consistent builds every time.
  - **Dependency Management**: Manages libraries and dependencies, ensuring the app has all the necessary modules to run smoothly.

- **Google Maps API**:
  - **Mapping Services**: Provides map views, location search, and route calculation, essential for a carpooling app.
  - **Geolocation**: Allows the app to determine the user's current location, aiding in trip planning and coordination.

- **Material Design**:
  - **UI Guidelines**: A design system from Google, ensuring the app has a modern and consistent user interface.
  - **Components**: Provides a range of UI components, from buttons to sliders, ensuring a cohesive user experience. 

## Installation & Deployment

To immerse yourself in the Irish Road Shares experience:

1. Clone the repository:
git clone https://github.com/PabloSimonEstrada/SDAassign5_2023_PabloSimonEstrada.
2. Launch Android Studio and open the cloned project.
3. Deploy the application on an Android device or emulator to witness its capabilities.

## Viewing the App

Designed with precision, Irish Road Shares is optimized for Android devices, ensuring a responsive and immersive user experience.

## Author

Pablo Simon Estrada - A software developer with a passion for creating impactful Android applications.

## References

- [Android Developers Documentation](https://developer.android.com/docs)
- [Firestore Documentation](https://firebase.google.com/docs/firestore)
- [Stack Overflow](https://stackoverflow.com)
- [Codepath Android Cliffnotes](https://guides.codepath.com/android/using-the-recyclerview)
- [Vogella](https://www.vogella.com/tutorials/AndroidIntent/article.html)
- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [YouTube](https://www.youtube.com)
- [Canva](https://www.canva.com/)
- [Google Developers Documentation - Directions API](https://developers.google.com/maps/documentation/directions/start)
- [Google Developers Documentation - Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/start)
- [Google Developers Documentation - Places API](https://developers.google.com/places/web-service/intro)
- [Google Cloud Documentation](https://cloud.google.com/docs)
- [Android Developers Documentation](https://developer.android.com/guide/topics/connectivity)
- [Google Cloud Community Tutorials](https://cloud.google.com/community/tutorials)

