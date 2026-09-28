# SmartSpend

SmartSpend is a backend expense management system built using Spring Boot.

The system helps users track expenses, manage budgets, monitor saving goals, analyze receipt images using AI, and review spending behavior through multiple analytical endpoints.

---

## Project Idea

Managing personal expenses manually can be difficult.

Users may:

- Forget their expenses
- Lose receipts
- Exceed their budgets
- Have difficulty understanding where their money goes
- Struggle to track saving goals

SmartSpend solves these problems by providing one backend system for managing and analyzing personal expenses.

---

## Main Features

- User management
- Expense categories
- Monthly and yearly budgets
- Manual expense tracking
- Saving goals
- AI receipt analysis
- Automatic receipt classification
- Spending analytics
- Budget forecasting
- Saving goal progress tracking
- Email budget status notifications

---

## Technologies Used

### Backend
- Java
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Maven

### Database
- MySQL

### AI
- Gemini API

Gemini analyzes receipt images and extracts:

- Store name
- Total amount
- Purchase date
- Expense category

### Email
- Spring Mail
- SMTP

Used to send budget status notifications to users.

### Testing
- Postman

---

## Main Models

The system contains six main models:

### User

Stores user information.

Main fields:

- id
- name
- email
- password
- createdAt

---

### Category

Represents expense categories.

Examples:

- Restaurants
- Groceries
- Transportation
- Shopping
- Entertainment
- Bills
- Health
- Education
- Travel

---

### Budget

Represents the user's spending budget.

Main fields:

- id
- amount
- period
- startDate
- user_id

Supported periods:

- MONTHLY
- YEARLY

The budget automatically works in cycles based on the start date.

---

### ManualExpense

Represents expenses entered manually by the user.

Main fields:

- id
- description
- amount
- expenseDate
- user_id
- category_id

---

### Receipt

Represents expenses created by uploading receipt images.

Main fields:

- id
- storeName
- totalAmount
- purchaseDate
- user_id
- category_id

The user only uploads the receipt image.

Gemini AI extracts the receipt information automatically.

---

### SavingGoal

Represents a financial saving goal.

Main fields:

- id
- name
- targetAmount
- savedAmount
- targetDate
- user_id

---

## AI Receipt Analysis

SmartSpend uses Gemini AI to analyze receipt images.

The process is:

Receipt Image  
→ Convert image to Base64  
→ Send image and prompt to Gemini API  
→ Receive structured JSON  
→ Convert JSON into ReceiptAIResponse DTO  
→ Validate category and user  
→ Save Receipt in MySQL

Example AI result:

```json
{
  "storeName": "Katana Sushi",
  "totalAmount": 153.71,
  "purchaseDate": "2018-05-11",
  "category": "Restaurants"
}
