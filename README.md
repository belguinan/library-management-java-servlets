# Library Management System - JAVA EE Servlets

## Overview
A Java EE library management web project that enables librarians to manage books, track borrowings, and handle customers...

## Key Features
- Book inventory management with stock tracking
- Borrowing system with due dates
- Customers tracking
- Search and filtering capabilities
- Borrowing history

## Tech Stack
- **Backend**: Java EE (Servlets, JPA, Repository Pattern)
- **Frontend**: Vanilla JavaScript, Bootstrap
- **Database**: JPA/Hibernate
- **Login**: Session / WebFilters

## Architecture
```
.
├── Repositories
│   ├── DatabaseRepository<T>
│   │   └── create(), update(), delete(), find(), paginate()
│   ├── BookRepository extends DatabaseRepository<Book>
│   ├── BorrowRepository extends DatabaseRepository<Borrow>
│   ├── CustomerRepository extends DatabaseRepository<Customer>
│   └── UserRepository extends DatabaseRepository<User>
├── Entities
│   ├── Book extends JpaTimestamp
│   ├── Borrow extends JpaTimestamp
│   ├── Customer extends JpaTimestamp
│   └── User extends JpaTimestamp
├── Factories
│   ├── BookFactory extends ModelValidator
│   ├── BorrowFactory extends ModelValidator
│   ├── CustomerFactory extends ModelValidator
│   └── UserFactory extends ModelValidator
└── Servlets
    ├── HttpBaseController
    │   └── index(), store(), update(), delete(), json(), view()
    ├── BookServlet extends HttpBaseController
    ├── BorrowServlet extends HttpBaseController
    ├── CustomerServlet extends HttpBaseController
    ├── LoginServlet extends HttpBaseController
    └── HomeServlet extends HttpBaseController
```
